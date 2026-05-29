package com.example.bank.controller;

import com.example.bank.model.Transaction;
import com.example.bank.model.TransferRequest;
import com.example.bank.repository.TransactionRepository;
import com.example.bank.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransferService transferService;
    private final TransactionRepository transactionRepository;

    public TransactionController(TransferService transferService, TransactionRepository transactionRepository) {
        this.transferService = transferService;
        this.transactionRepository = transactionRepository;
    }

    // 3. POST /transactions : Проведение перевода денег (Асинхронно!)
    @PostMapping
    public CompletableFuture<ResponseEntity<Map<String, String>>> makeTransfer(@RequestBody TransferRequest request) {
        // Вызываем асинхронный метод сервиса (наш аналог горутины)
        return transferService.makeTransfer(request.getFromAccount(), request.getToAccount(), request.getAmount())
                .thenApply(tx -> ResponseEntity.ok(Map.of(
                        "transactionId", tx.getId(),
                        "status", tx.getStatus()
                )))
                // Если внутри CompletableFuture произошла ошибка (например, IllegalArgumentException)
                .exceptionally(ex -> ResponseEntity.badRequest().body(Map.of(
                        "status", "failed",
                        "error", ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage()
                )));
    }

    // 4. GET /transactions/{accountId} : Получение истории транзакций
    @GetMapping("/{accountId}")
    public ResponseEntity<List<Transaction>> getHistory(@PathVariable String accountId) {
        List<Transaction> history = transactionRepository.findByFromAccountOrToAccount(accountId, accountId);
        return ResponseEntity.ok(history);
    }
}
