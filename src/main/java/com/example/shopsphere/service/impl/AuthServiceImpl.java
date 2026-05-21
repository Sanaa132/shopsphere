package com.example.shopsphere.service.impl;

import com.example.shopsphere.dto.request.LoginRequest;
import com.example.shopsphere.dto.request.RegisterRequest;
import com.example.shopsphere.dto.response.AuthResponse;
import com.example.shopsphere.entity.Role;
import com.example.shopsphere.entity.User;
import com.example.shopsphere.exception.BadRequestException;
import com.example.shopsphere.exception.DuplicateResourceException;
import com.example.shopsphere.exception.ResourceNotFoundException;
import com.example.shopsphere.repository.UserRepository;
import com.example.shopsphere.security.JwtUtil;
import com.example.shopsphere.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    // =========================
    // REGISTER
    // =========================
    @Override
    public AuthResponse register(RegisterRequest request) {

        // Check duplicate email
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already exists");
        }

        // Create user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        // Build UserDetails (FIXED: Prefix concatenation string removed to avoid dual mapping errors)
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                savedUser.getEmail(),
                savedUser.getPassword(),
                Collections.singleton(
                        new SimpleGrantedAuthority(savedUser.getRole().name())
                )
        );

        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(
                token,
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
    }

    // ==========================================
    // LOGIN (UPDATED AUTHORITY DIALECT ENGINE)
    // ==========================================
    @Override
    public AuthResponse login(LoginRequest request) {

        // STEP 1: Check if user exists
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("No account found with this email")
                );

        // STEP 2: Validate password manually
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Incorrect password");
        }

        // STEP 3: Authenticate Spring Security context
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // STEP 4: Build UserDetails for JWT (FIXED: Prefix concatenation string removed)
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(
                        new SimpleGrantedAuthority(user.getRole().name())
                )
        );

        // STEP 5: Generate JWT
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }
}