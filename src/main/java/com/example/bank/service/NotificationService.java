package com.example.bank.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void notify(String transactionId, String status, String message) {
        log.info("[Уведомление] Транзакция {}: {} - {}", transactionId, status, message);
    }
}
