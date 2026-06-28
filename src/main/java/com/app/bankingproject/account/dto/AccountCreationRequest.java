package com.app.bankingproject.account.dto;

import com.karthi.bank730.account.enums.AccountType;

import java.math.BigDecimal;

public record AccountCreationRequest(
        String customerId,
        AccountType accountType,
        BigDecimal initialDeposit
) {

}