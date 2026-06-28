package com.app.bankingproject.account.dto;

import com.app.bankingproject.account.enums.AccountType;

import java.math.BigDecimal;

public record AccountCreationRequest(
        String customerId,
        AccountType accountType,
        BigDecimal initialDeposit
) {

}