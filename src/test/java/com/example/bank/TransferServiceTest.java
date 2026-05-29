package com.example.bank;

import com.example.bank.model.BankAccount;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.service.TransferService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferServiceTest {

    // Создаем "поддельные" репозитории (Mock), чтобы не зависеть от реальной БД во время юнит-теста
    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final TransactionRepository transactionRepository = mock(TransactionRepository.class);
    private final TransferService transferService = new TransferService(accountRepository, transactionRepository);

    @Test
    void testSuccessfulTransfer() throws Exception {
        // У отправителя 500 USD, у получателя 100 USD
        BankAccount fromAcc = new BankAccount("123", "John Doe", "USD");
        fromAcc.setBalance(BigDecimal.valueOf(500.0));

        BankAccount toAcc = new BankAccount("456", "Jane Doe", "USD");
        toAcc.setBalance(BigDecimal.valueOf(100.0));

        // Обучаем заглушки возвращать наши аккаунты при поиске по ID
        when(accountRepository.findById("123")).thenReturn(Optional.of(fromAcc));
        when(accountRepository.findById("456")).thenReturn(Optional.of(toAcc));

        // Переводим 200 USD и ждем завершения асинхронного потока через .get()
        var tx = transferService.makeTransfer("123", "456", BigDecimal.valueOf(200.0)).get();

        // Проверяем критерии: балансы должны измениться, статус транзакции - completed
        assertEquals(BigDecimal.valueOf(300.0), fromAcc.getBalance());
        assertEquals(BigDecimal.valueOf(300.0), toAcc.getBalance());
        assertEquals("completed", tx.getStatus());
    }

    @Test
    void testInsufficientFundsTransfer() {
        // У отправителя 50 USD, а перевести пытается 100 USD
        BankAccount fromAcc = new BankAccount("123", "John Doe", "USD");
        fromAcc.setBalance(BigDecimal.valueOf(50.0));

        BankAccount toAcc = new BankAccount("456", "Jane Doe", "USD");

        when(accountRepository.findById("123")).thenReturn(Optional.of(fromAcc));
        when(accountRepository.findById("456")).thenReturn(Optional.of(toAcc));

        // Проверяем, что метод выбросит ошибку из-за нехватки средств
        assertThrows(Exception.class, () -> {
            transferService.makeTransfer("123", "456", BigDecimal.valueOf(100.0)).get();
        });
    }
    @Test
    void testTransferWithCurrencyConversion() throws Exception {
        // Отправитель в USD, получатель в EUR
        BankAccount fromAcc = new BankAccount("123", "John Doe", "USD");
        fromAcc.setBalance(BigDecimal.valueOf(100.0));

        BankAccount toAcc = new BankAccount("456", "Jane Doe", "EUR");
        toAcc.setBalance(BigDecimal.valueOf(0.0));

        when(accountRepository.findById("123")).thenReturn(Optional.of(fromAcc));
        when(accountRepository.findById("456")).thenReturn(Optional.of(toAcc));

        // Переводим 100 USD. По нашему курсу (0.92) на евровый счет должно упасть 92 EUR
        transferService.makeTransfer("123", "456", BigDecimal.valueOf(100.0)).get();

        // Проверяем: с долларового счета списалось 100, на евровый зачислилось 92
        // Вместо assertEquals используем compareTo == 0 для точного сравнения денег
        assertTrue(BigDecimal.valueOf(0.0).compareTo(fromAcc.getBalance()) == 0);
        assertTrue(BigDecimal.valueOf(92.0).compareTo(toAcc.getBalance()) == 0);
    }
}