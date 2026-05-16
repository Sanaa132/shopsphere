package com.example.shopsphere.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
    private String phoneNumber;
}