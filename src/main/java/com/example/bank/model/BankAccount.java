package com.example.bank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity
public class BankAccount {

    @Id 
    private String id;
    private String accountHolder;
    private BigDecimal balance;
    private String currency;

    public BankAccount() {}

    public BankAccount(String id, String accountHolder, String currency) {
        this.id = id;
        this.accountHolder = accountHolder;
        this.balance = BigDecimal.ZERO;
        this.currency = currency;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAccountHolder() { return accountHolder; }
    public void setAccountHolder(String accountHolder) { this.accountHolder = accountHolder; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
