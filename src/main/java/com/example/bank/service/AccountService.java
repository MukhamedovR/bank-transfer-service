package com.example.bank.service;

import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.model.BankAccount;
import com.example.bank.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public BankAccount createAccount(String accountHolder, String currency) {
        String id = UUID.randomUUID().toString();
        BankAccount account = new BankAccount(id, accountHolder, currency);
        return accountRepository.save(account);
    }

    public BankAccount getAccount(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + id));
    }

    @Transactional
    public void updateAccount(BankAccount account) {
        accountRepository.save(account);
    }
}
