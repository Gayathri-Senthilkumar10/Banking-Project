package com.app.bankingproject.account.controller;

import com.app.bankingproject.account.dto.AccountCreationRequest;
import com.app.bankingproject.account.dto.AccountResponse;
import com.app.bankingproject.account.entity.Account;
import com.app.bankingproject.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    final private AccountService accountService;

    @RequestMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountCreationRequest accountCreationRequest) {
        return new ResponseEntity<>(accountService.createAccount(accountCreationRequest), HttpStatus.CREATED);
    }

    @RequestMapping("/all/{customerId}")
    public ResponseEntity<List<AccountResponse>> getAllAccounts(@PathVariable String customerId) {
        return new ResponseEntity<>(accountService.getAllAccountForCustomer(customerId), HttpStatus.OK);
    }

    @RequestMapping("/details/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccountDetails(
            @PathVariable String accountNumber) {

        return new ResponseEntity<>(
                accountService.getAccountByAccountNumber(accountNumber),
                HttpStatus.OK);
    }
}

