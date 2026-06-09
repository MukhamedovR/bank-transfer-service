package com.example.bank;

import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.model.BankAccount;
import com.example.bank.model.Transaction;
import com.example.bank.model.TransactionStatus;
import com.example.bank.service.AccountService;
import com.example.bank.service.NotificationService;
import com.example.bank.service.TransactionService;
import com.example.bank.service.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock private AccountService accountService;
    @Mock private TransactionService transactionService;
    @Mock private NotificationService notificationService;
    @InjectMocks private TransferService transferService;

    private BankAccount fromAccount;
    private BankAccount toAccount;
    private Transaction successTx;

    @BeforeEach
    void setUp() {
        fromAccount = new BankAccount("123", "John", "USD");
        fromAccount.setBalance(BigDecimal.valueOf(500));
        toAccount = new BankAccount("456", "Jane", "USD");
        toAccount.setBalance(BigDecimal.valueOf(100));
        successTx = new Transaction("tx1", "123", "456", BigDecimal.valueOf(200), LocalDateTime.now(), TransactionStatus.COMPLETED.getValue());
    }

    @Test
    void testSuccessfulTransfer() {
        when(accountService.getAccount("123")).thenReturn(fromAccount);
        when(accountService.getAccount("456")).thenReturn(toAccount);
        when(transactionService.recordTransaction(eq("123"), eq("456"), eq(BigDecimal.valueOf(200)), eq(TransactionStatus.COMPLETED.getValue()), any(LocalDateTime.class)))
                .thenReturn(successTx);

        CompletableFuture<Transaction> future = transferService.makeTransfer("123", "456", BigDecimal.valueOf(200));
        Transaction result = future.join();

        assertEquals("tx1", result.getId());
        assertEquals(TransactionStatus.COMPLETED.getValue(), result.getStatus());
        assertEquals(BigDecimal.valueOf(300), fromAccount.getBalance());
        assertEquals(BigDecimal.valueOf(300), toAccount.getBalance());
        verify(accountService).updateAccount(fromAccount);
        verify(accountService).updateAccount(toAccount);
        verify(notificationService).notify(eq("tx1"), eq("completed"), anyString());
    }

    @Test
    void testInsufficientFunds() {
        fromAccount.setBalance(BigDecimal.valueOf(50));
        when(accountService.getAccount("123")).thenReturn(fromAccount);
        when(accountService.getAccount("456")).thenReturn(toAccount);
        when(transactionService.recordTransaction(eq("123"), eq("456"), eq(BigDecimal.valueOf(100)), eq(TransactionStatus.FAILED.getValue()), any(LocalDateTime.class)))
                .thenReturn(new Transaction("tx2", "123", "456", BigDecimal.valueOf(100), LocalDateTime.now(), TransactionStatus.FAILED.getValue()));

        CompletableFuture<Transaction> future = transferService.makeTransfer("123", "456", BigDecimal.valueOf(100));
        assertThrows(CompletionException.class, future::join);
        verify(notificationService).notify(anyString(), eq("failed"), anyString());
        verify(accountService, never()).updateAccount(any());
    }

    @Test
    void testAccountNotFound() {
        when(accountService.getAccount("999")).thenThrow(new AccountNotFoundException("Not found"));
        when(transactionService.recordTransaction(eq("999"), eq("456"), any(), eq(TransactionStatus.FAILED.getValue()), any(LocalDateTime.class)))
                .thenReturn(new Transaction("tx3", "999", "456", BigDecimal.valueOf(100), LocalDateTime.now(), TransactionStatus.FAILED.getValue()));

        CompletableFuture<Transaction> future = transferService.makeTransfer("999", "456", BigDecimal.valueOf(100));
        assertThrows(CompletionException.class, future::join);
        verify(notificationService).notify(anyString(), eq("failed"), contains("Not found"));
    }
}