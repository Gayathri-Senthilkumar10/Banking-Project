package com.app.bankingproject.customer.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customers")
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,  nullable = false)
    private String customerId;

    private String fullName;
    @Column(unique = true)
    private String email;
    private String mobileNumber;
    private String address;
    private Integer age;
    private boolean isMinor;

    @Column(unique = true)
    private String PAN;

    @Column(unique = true)
    private String aadhaarNumber;
}
