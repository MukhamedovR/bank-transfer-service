package com.example.bank.controller;

import com.example.bank.dto.TransferRequest;
import com.example.bank.dto.TransferResponse;
import com.example.bank.model.Transaction;
import com.example.bank.service.TransactionService;
import com.example.bank.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransferService transferService;
    private final TransactionService transactionService;

    public TransactionController(TransferService transferService, TransactionService transactionService) {
        this.transferService = transferService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<TransferResponse>> makeTransfer(@Valid @RequestBody TransferRequest request) {
        return transferService.makeTransfer(request.getFromAccount(), request.getToAccount(), request.getAmount())
                .thenApply(tx -> ResponseEntity.ok(new TransferResponse(tx.getId(), tx.getStatus())))
                .exceptionally(ex -> {
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    return ResponseEntity.badRequest().body(new TransferResponse(null, "failed"));
                });
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<Transaction>> getHistory(@PathVariable String accountId) {
        List<Transaction> history = transactionService.getHistory(accountId);
        return ResponseEntity.ok(history);
    }
}
