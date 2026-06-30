package com.app.bankingproject.customer.service;

import com.app.bankingproject.customer.dto.CustomerCreationRequest;
import com.app.bankingproject.customer.dto.CustomerResponse;
import com.app.bankingproject.customer.entity.Customer;
import com.app.bankingproject.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerResponse createNewCustomer(CustomerCreationRequest customerCreationRequest){

//  Check the user is already registered or not
        if(customerRepository.existsByEmailAndAadhaarNumber(
                customerCreationRequest.email(),customerCreationRequest.pan()
        )){
            throw  new RuntimeException("Customer Already Exists");
        }
        Customer customer = new Customer();
        customer.setCustomerId(generateCustomerId()); // Check the method for auto generate logic
        customer.setFullName(customerCreationRequest.fullName());
        customer.setEmail(customerCreationRequest.email());
        customer.setMobileNumber( customerCreationRequest.mobileNumber());
        customer.setAddress( customerCreationRequest.address());
        customer.setAge(customerCreationRequest.age());
        customer.setMinor(
                customerCreationRequest.age() < 18 ? true : false
        );
        customer.setPAN(customerCreationRequest.pan());
        customer.setAadhaarNumber( customerCreationRequest.aadhaarNumber());

        Customer savedCustomer = customerRepository.save(customer);

        return mapToCustomerResponse(savedCustomer);
    }

    public String generateCustomerId(){
        int year = Year.now().getValue();

        String latestCustomerId = customerRepository.findTopByOrderByIdDesc().map(
//              customer -> customer.getCustomerId()
                Customer::getCustomerId
        ).orElse(null);

        long sequence = 1;
        System.out.println(latestCustomerId);
        if(latestCustomerId != null && latestCustomerId.startsWith("GK"+year)){
            sequence = Long.parseLong(latestCustomerId.substring(6));
            sequence++;
        }
        System.out.println(sequence);
        return "GK"+year+sequence;
    }

    public CustomerResponse findCustomerById(String customerId) {
        return mapToCustomerResponse(
                customerRepository.findByCustomerId(customerId).orElseThrow(
                        () -> new RuntimeException("Customer Not Found")
                )
        );

    }

    public CustomerResponse mapToCustomerResponse(Customer customer){
        return  new CustomerResponse(
                customer.getCustomerId(),
                customer.getFullName()
        );
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream().map(cust ->
                mapToCustomerResponse(cust)
        ).toList();
    }
}
