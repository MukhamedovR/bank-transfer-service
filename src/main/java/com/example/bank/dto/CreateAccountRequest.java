package com.example.bank.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateAccountRequest {
    @NotBlank(message = "Account holder is required")
    private String accountHolder;

    @NotBlank(message = "Currency is required")
    private String currency;

    public String getAccountHolder() { return accountHolder; }
    public void setAccountHolder(String accountHolder) { this.accountHolder = accountHolder; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
