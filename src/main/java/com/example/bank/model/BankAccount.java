package com.example.bank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Entity // Эта аннотация говорит Spring, что на основе этого класса нужно создать таблицу в базе данных
public class BankAccount {

    @Id // Указывает, что это поле — уникальный идентификатор (Primary Key)
    private String id;
    private String accountHolder;
    private BigDecimal balance;
    private String currency;

    // Пустой конструктор (обязателен для работы с базой данных)
    public BankAccount() {}

    // Конструктор для удобного создания нового счета
    public BankAccount(String id, String accountHolder, String currency) {
        this.id = id;
        this.accountHolder = accountHolder;
        this.balance = BigDecimal.ZERO; // По ТЗ начальный баланс всегда равен 0
        this.currency = currency;
    }

    // Геттеры и сеттеры (нужны, чтобы Spring мог читать и изменять данные)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAccountHolder() { return accountHolder; }
    public void setAccountHolder(String accountHolder) { this.accountHolder = accountHolder; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
}
