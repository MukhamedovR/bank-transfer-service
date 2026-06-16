package com.example.bank.service;

import com.example.bank.model.Transaction;
import com.example.bank.model.TransactionStatus;
import com.example.bank.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction recordTransaction(String fromAccount, String toAccount, BigDecimal amount, String status, LocalDateTime timestamp) {
        return transactionRepository.save(
                new Transaction(UUID.randomUUID().toString(), fromAccount, toAccount, amount, timestamp, status));
    }

    public List<Transaction> getHistory(String accountId) {
        return transactionRepository.findByFromAccountOrToAccountOrderByTimestampDesc(accountId, accountId);
    }
}
