package com.example.bank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity // Создаст таблицу для транзакций в базе данных
public class Transaction {

    @Id
    private String id;
    private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String timestamp;
    private String status; // "completed" или "failed"

    // Пустой конструктор
    public Transaction() {}

    // Конструктор для создания транзакции
    public Transaction(String id, String fromAccount, String toAccount, BigDecimal amount, String timestamp, String status) {
        this.id = id;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.timestamp = timestamp;
        this.status = status;
    }

    // Геттеры и сеттеры
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFromAccount() { return fromAccount; }
    public void setFromAccount(String fromAccount) { this.fromAccount = fromAccount; }

    public String getToAccount() { return toAccount; }
    public void setToAccount(String toAccount) { this.toAccount = toAccount; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
