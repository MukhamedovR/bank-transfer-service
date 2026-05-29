package com.example.bank.repository;

import com.example.bank.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// JpaRepository<BankAccount, String> означает: работаем со счетами, у которых ID — это String
public interface AccountRepository extends JpaRepository<BankAccount, String> {
}
