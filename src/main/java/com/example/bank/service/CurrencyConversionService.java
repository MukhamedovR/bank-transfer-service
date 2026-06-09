package com.example.bank.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class CurrencyConversionService {

    private static final Map<String, BigDecimal> RATES = Map.of(
            "USD", BigDecimal.ONE,
            "EUR", new BigDecimal("0.92"),
            "GBP", new BigDecimal("0.79"),
            "JPY", new BigDecimal("151.23")
    );

    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equalsIgnoreCase(toCurrency)) {
            return amount;
        }
        BigDecimal rateFrom = RATES.get(fromCurrency.toUpperCase());
        BigDecimal rateTo = RATES.get(toCurrency.toUpperCase());

        if (rateFrom == null) {
            throw new IllegalArgumentException("Unsupported currency: " + fromCurrency);
        }
        if (rateTo == null) {
            throw new IllegalArgumentException("Unsupported currency: " + toCurrency);
        }

        BigDecimal inUsd = amount.divide(rateFrom, 10, RoundingMode.HALF_EVEN);
        BigDecimal result = inUsd.multiply(rateTo);
        return result.setScale(2, RoundingMode.HALF_EVEN);
    }
}
