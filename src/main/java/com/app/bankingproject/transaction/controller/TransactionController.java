package com.app.bankingproject.transaction.controller;

import com.app.bankingproject.transaction.dto.DepositRequest;
import com.app.bankingproject.transaction.dto.TransactionResponse;
import com.app.bankingproject.transaction.dto.TransferRequest;
import com.app.bankingproject.transaction.dto.WithdrawRequest;
import com.app.bankingproject.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    final private TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest  depositRequest) throws InterruptedException {
        return new ResponseEntity<>(transactionService.deposit(depositRequest), HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawRequest withdrawRequest){
        return new ResponseEntity<>(transactionService.withdraw(withdrawRequest),HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest transferRequest){
        return new ResponseEntity<>(transactionService.transfer(transferRequest),HttpStatus.OK);
    }
}