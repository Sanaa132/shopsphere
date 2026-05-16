package com.example.shopsphere.service;

import com.example.shopsphere.dto.request.LoginRequest;
import com.example.shopsphere.dto.request.RegisterRequest;
import com.example.shopsphere.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}