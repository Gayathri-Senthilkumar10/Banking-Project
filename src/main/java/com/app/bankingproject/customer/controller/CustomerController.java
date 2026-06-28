package com.app.bankingproject.customer.controller;

import com.app.bankingproject.customer.dto.CustomerCreationRequest;
import com.app.bankingproject.customer.dto.CustomerResponse;
import com.app.bankingproject.customer.entity.Customer;
import com.app.bankingproject.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerCreationRequest customerCreationRequest){
        return new ResponseEntity<>(
                customerService.createNewCustomer(customerCreationRequest), HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(){
        return new ResponseEntity<>(
                customerService.getAllCustomers(),HttpStatus.OK
        );
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable String customerId){
        return new ResponseEntity<>(
                customerService.findCustomerById(customerId),HttpStatus.OK
        );
    }


}