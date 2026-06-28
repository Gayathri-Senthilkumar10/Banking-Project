package com.app.bankingproject.account.service;

import com.app.bankingproject.account.dto.AccountCreationRequest;
import com.app.bankingproject.account.dto.AccountResponse;
import com.app.bankingproject.account.entity.Account;
import com.app.bankingproject.account.repository.AccountRepository;
import com.app.bankingproject.customer.entiry.Customer;
import com.app.bankingproject.customer.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    final private CustomerRepository customerRepository;
    final private AccountRepository accountRepository;


    public AccountResponse createAccount(AccountCreationRequest accountCreationRequest){
        Customer existingCustomer = customerRepository.findByCustomerId(accountCreationRequest.customerId())
                .orElseThrow(()-> new RuntimeException("Customer not found"));

        Account account =  Account.builder()
                .accountType(accountCreationRequest.accountType())
                .accountNumber(generateAccountNumber(existingCustomer.getId()))
                .balance(accountCreationRequest.initialDeposit())
                .active(true)
                .customer(existingCustomer).build();
        accountRepository.save(account);

        return mapToAccountResponse(account);
    }

    public String generateAccountNumber(Long id){
        String randomPart =  UUID.randomUUID().toString().substring(0, 8);
        return "ACC"+ Year.now().getValue()+randomPart+String.format("%06d",id);
    }


    public AccountResponse mapToAccountResponse(Account account){
        return  new AccountResponse(
                account.getAccountNumber(),
                account.getCustomer().getFullName(),
                account.getAccountType(),
                account.getBalance()
        );
    }

    public List<AccountResponse> getAllAccountForCustomer(String customerId) {
        return accountRepository.getAllByCustomer_CustomerId(customerId).stream()
                .map(
                        account -> mapToAccountResponse(account)
                ).toList();
    }

    public AccountResponse getAccountByAccountNumber(String accountNumber) {

        Account account = accountRepository
                .findAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return mapToAccountResponse(account);
    }
}