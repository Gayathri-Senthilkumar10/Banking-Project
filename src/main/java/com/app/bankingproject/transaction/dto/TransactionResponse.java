package com.app.bankingproject.transaction.dto;

import com.app.bankingproject.transaction.enums.TransactionType;

import java.math.BigDecimal;

public record TransactionResponse(
        String reference,
        TransactionType type,
        BigDecimal amount,
        BigDecimal balanceAfter
) {
}