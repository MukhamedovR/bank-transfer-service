package com.example.bank.model;

import java.math.BigDecimal;

public enum Currency {
    USD(BigDecimal.ONE),
    EUR(new BigDecimal("0.92")),
    GBP(new BigDecimal("0.79")),
    JPY(new BigDecimal("151.23"));

    private final BigDecimal rate;

    Currency(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public static Currency fromCode(String code) {
        try {
            return Currency.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Unsupported currency: " + code);
        }
    }
}
