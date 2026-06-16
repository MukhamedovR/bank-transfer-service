package com.example.bank.service;

import com.example.bank.model.Currency;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CurrencyConversionService {

    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equalsIgnoreCase(toCurrency)) {
            return amount;
        }

        BigDecimal rateFrom = Currency.fromCode(fromCurrency).getRate();
        BigDecimal rateTo = Currency.fromCode(toCurrency).getRate();

        BigDecimal inUsd = amount.divide(rateFrom, 10, RoundingMode.HALF_EVEN);
        BigDecimal result = inUsd.multiply(rateTo);
        return result.setScale(2, RoundingMode.HALF_EVEN);
    }
}
