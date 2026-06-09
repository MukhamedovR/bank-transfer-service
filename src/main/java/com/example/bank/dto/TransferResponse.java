package com.example.bank.dto;

public class TransferResponse {
    private String transactionId;
    private String status;

    public TransferResponse(String transactionId, String status) {
        this.transactionId = transactionId;
        this.status = status;
    }

    public String getTransactionId() { return transactionId; }
    public String getStatus() { return status; }
}