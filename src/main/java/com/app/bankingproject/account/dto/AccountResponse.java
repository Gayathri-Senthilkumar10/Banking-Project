package com.app.bankingproject.account.dto;

import com.app.bankingproject.account.enums.AccountType;

import java.math.BigDecimal;

public record AccountResponse(
        String accountNumber,
        String customerName,
        AccountType accountType,
        BigDecimal balance
) {
}