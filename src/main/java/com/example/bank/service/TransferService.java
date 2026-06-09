package com.example.bank.service;

import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.InsufficientFundsException;
import com.example.bank.model.BankAccount;
import com.example.bank.model.Transaction;
import com.example.bank.model.TransactionStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Service
public class TransferService {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final NotificationService notificationService;

    public TransferService(AccountService accountService, TransactionService transactionService, NotificationService notificationService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.notificationService = notificationService;
    }

    @Async
    @Transactional
    public CompletableFuture<Transaction> makeTransfer(String fromId, String toId, BigDecimal amount) {
        LocalDateTime timestamp = LocalDateTime.now();
        try {
            if (fromId.equals(toId)) {
                throw new IllegalArgumentException("Cannot transfer to the same account");
            }

            BankAccount fromAccount = accountService.getAccount(fromId);
            BankAccount toAccount = accountService.getAccount(toId);

            if (fromAccount.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds");
            }

            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            toAccount.setBalance(toAccount.getBalance().add(amount));

            // 👇 ЭТИ ДВЕ СТРОКИ НУЖНО ДОБАВИТЬ
            accountService.updateAccount(fromAccount);
            accountService.updateAccount(toAccount);
            // 👆

            Transaction tx = transactionService.recordTransaction(fromId, toId, amount, TransactionStatus.COMPLETED.getValue(), timestamp);
            notificationService.notify(tx.getId(), "completed", "Transfer successful");
            return CompletableFuture.completedFuture(tx);
        } catch (Exception e) {
            Transaction failedTx = transactionService.recordTransaction(fromId, toId, amount, TransactionStatus.FAILED.getValue(), timestamp);
            notificationService.notify(failedTx.getId(), "failed", e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }
}