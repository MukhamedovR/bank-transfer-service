package com.example.bank.controller;

import com.example.bank.dto.BalanceResponse;
import com.example.bank.dto.CreateAccountRequest;
import com.example.bank.model.BankAccount;
import com.example.bank.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<BankAccount> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        BankAccount account = accountService.createAccount(request.getAccountHolder(), request.getCurrency());
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable String id) {
        BankAccount account = accountService.getAccount(id);
        return ResponseEntity.ok(new BalanceResponse(account.getBalance()));
    }
}
