package com.example.bank.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    public void notify(String transactionId, String status, String message) {
        log.info("[Уведомление] Транзакция {}: {} - {}", transactionId, status, message);
    }
}
