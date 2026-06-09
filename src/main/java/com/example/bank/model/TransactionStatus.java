package com.example.bank.model;

public enum TransactionStatus {
    COMPLETED("completed"),
    FAILED("failed");

    private final String value;

    TransactionStatus(String value) { this.value = value; }

    public String getValue() { return value; }
}
