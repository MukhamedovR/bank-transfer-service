package com.example.bank.service;

import com.example.bank.model.BankAccount;
import com.example.bank.model.Transaction;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    // Внедряем репозитории через конструктор
    public TransferService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Асинхронный метод для проведения перевода (Аналог горутины в Go).
     *
     * @Async отправляет выполнение в виртуальный поток.
     * CompletableFuture — это аналог канала, через который мы вернем результат или ошибку.
     */
    @Async
    @Transactional
    public CompletableFuture<Transaction> makeTransfer(String fromId, String toId, BigDecimal amount) {
        String transactionId = UUID.randomUUID().toString();
        String timestamp = Instant.now().toString();

        try {
            BankAccount fromAccount = accountRepository.findById(fromId)
                    .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
            BankAccount toAccount = accountRepository.findById(toId)
                    .orElseThrow(() -> new IllegalArgumentException("Receiver account not found"));

            if (fromAccount.getBalance().compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient funds");
            }

            // --- ЛОГИКА КОНВЕРТАЦИИ ВАЛЮТ (Доп. задание) ---
            BigDecimal finalAmountToDeposit = amount;

            // Если валюты разные, конвертируем (например, базовый курс: 1 USD = 0.92 EUR)
            if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
                double rate = 1.0;
                if (fromAccount.getCurrency().equals("USD") && toAccount.getCurrency().equals("EUR")) {
                    rate = 0.92;
                } else if (fromAccount.getCurrency().equals("EUR") && toAccount.getCurrency().equals("USD")) {
                    rate = 1.08;
                }
                finalAmountToDeposit = amount.multiply(BigDecimal.valueOf(rate));
            }
            // ------------------------------------------------

            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            toAccount.setBalance(toAccount.getBalance().add(finalAmountToDeposit));

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            Transaction successfulTx = new Transaction(transactionId, fromId, toId, amount, timestamp, "completed");
            transactionRepository.save(successfulTx);

            System.out.printf("[Уведомление] Перевод %s выполнен успешно!%n", transactionId);
            return CompletableFuture.completedFuture(successfulTx);

        } catch (Exception e) {
            Transaction failedTx = new Transaction(transactionId, fromId, toId, amount, timestamp, "failed");
            transactionRepository.save(failedTx);
            System.out.printf("[Уведомление] Ошибка перевода %s: %s%n", transactionId, e.getMessage());
            return CompletableFuture.failedFuture(e);
        }
    }
}