package com.app.bankingproject.transaction.service;

import com.app.bankingproject.account.dto.AccountResponse;
import com.app.bankingproject.account.entity.Account;
import com.app.bankingproject.account.repository.AccountRepository;
import com.app.bankingproject.transaction.dto.DepositRequest;
import com.app.bankingproject.transaction.dto.TransactionResponse;
import com.app.bankingproject.transaction.dto.TransferRequest;
import com.app.bankingproject.transaction.dto.WithdrawRequest;
import com.app.bankingproject.transaction.enums.TransactionStatus;
import com.app.bankingproject.transaction.enums.TransactionType;
import com.app.bankingproject.transaction.entity.Transaction;
import com.app.bankingproject.transaction.repository.TransactionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    final private AccountRepository accountRepository;
    final private TransactionRepository transactionRepository;


    //    deposit
    public TransactionResponse deposit(DepositRequest depositRequest) throws InterruptedException {
        Account account = accountRepository.findAccountByAccountNumber(depositRequest.accountNumber())
                .orElseThrow(
                        () -> new RuntimeException(depositRequest.accountNumber()+" not found")
                );

//        Thread.sleep(5000);
        BigDecimal before = account.getBalance();
        account.setBalance(
                before.add(depositRequest.amount())
        );
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.DEPOSIT)
                .amount(depositRequest.amount())
                .balanceBefore(before)
                .balanceAfter(account.getBalance())
                .transactionStatus(TransactionStatus.SUCCESS)
                .transactionTime(LocalDateTime.now())
                .transactionReference(generateTransactionReference()) // We need to Create
                .account(account).build();

        transactionRepository.save(transaction);
        return transactionMapper(transaction);
    }


    public TransactionResponse withdraw(@Valid WithdrawRequest withdrawRequest) {
        Account account = accountRepository.findAccountByAccountNumber(withdrawRequest.accountNumber())
                .orElseThrow(
                        () -> new RuntimeException(withdrawRequest.accountNumber()+" not found")
                );

        if(account.getBalance().compareTo(withdrawRequest.amount()) < 0){
            throw new RuntimeException("Insufficient funds");
        }

        BigDecimal before = account.getBalance();
        account.setBalance(
                before.subtract(withdrawRequest.amount())
        );
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .transactionType(TransactionType.WITHDRAW)
                .amount(withdrawRequest.amount())
                .balanceBefore(before)
                .balanceAfter(account.getBalance())
                .transactionStatus(TransactionStatus.SUCCESS)
                .transactionTime(LocalDateTime.now())
                .transactionReference(generateTransactionReference())
                .account(account).build();

        transactionRepository.save(transaction);
        return transactionMapper(transaction);
    }


    public TransactionResponse transfer(@Valid TransferRequest transferRequest) {
        // debit source account
        // credit destination account
        // create transactions records
        return null;
    }


    //    Generate Transaction Number
    public String generateTransactionReference(){
        return "TXN"+LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + UUID.randomUUID()
                .toString()
                .substring(0,4)
                .toUpperCase();
    }

//  Mapper to Transaction DTO

    public TransactionResponse transactionMapper(Transaction transaction){
        return  new TransactionResponse(
                transaction.getTransactionReference(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getBalanceAfter()
        );
    }


}