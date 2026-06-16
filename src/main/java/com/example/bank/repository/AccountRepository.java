package com.example.bank.repository;

import com.example.bank.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<BankAccount, String> {

    @Query("select a.balance from BankAccount a where a.id = :id")
    Optional<BigDecimal> findBalanceById(String id);
}
