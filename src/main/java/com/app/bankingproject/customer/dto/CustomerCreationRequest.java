package com.app.bankingproject.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CustomerCreationRequest(

        @NotBlank
        String fullName,
        @Email
        String email,
        String mobileNumber,
        String address,
        Integer age,
        @Pattern(
                regexp = "^[0-9]{12}$",
                message = "Invalid Aadhaar Number"
        )
        String aadhaarNumber,

        @Pattern(
                regexp = "^[A-z]{5}[0-9]{4}[A-Z]{1}",
                message = "Invalid PAN Number"
        )
        String pan

) {
}