package com.example.bank.repository;

import com.example.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    // Специальный метод: Spring сам сгенерирует SQL-запрос, который найдет все транзакции,
    // где указанный счет был либо отправителем (fromAccount), либо получателем (toAccount).
    List<Transaction> findByFromAccountOrToAccount(String fromAccount, String toAccount);
}
