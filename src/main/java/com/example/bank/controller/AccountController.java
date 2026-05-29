package com.example.bank.controller;

import com.example.bank.model.BankAccount;
import com.example.bank.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // 1. POST /accounts : Создание счета с начальным балансом 0
    @PostMapping
    public ResponseEntity<BankAccount> createAccount(@RequestBody BankAccount request) {
        String newId = UUID.randomUUID().toString(); // Генерируем уникальный ID
        BankAccount account = new BankAccount(newId, request.getAccountHolder(), request.getCurrency());
        BankAccount savedAccount = accountRepository.save(account);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    // 2. GET /accounts/{id}/balance : Получение баланса счета по его ID
    @GetMapping("/{id}/balance")
    public ResponseEntity<Map<String, Object>> getBalance(@PathVariable String id) {
        BankAccount account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        // Возвращаем ответ точно по вашему примеру данных
        return ResponseEntity.ok(Map.of("balance", account.getBalance()));
    }
}
