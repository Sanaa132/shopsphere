package com.example.shopsphere.service.impl;

import com.example.shopsphere.dto.request.LoginRequest;
import com.example.shopsphere.dto.request.RegisterRequest;
import com.example.shopsphere.dto.response.AuthResponse;
import com.example.shopsphere.entity.Role;
import com.example.shopsphere.entity.User;
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

    @Override
    public AuthResponse register(RegisterRequest request) {

        // CHECK DUPLICATE USER
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already exists");
        }

        // CREATE USER
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        // BUILD USER DETAILS FOR JWT
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                savedUser.getEmail(),
                savedUser.getPassword(),
                Collections.singleton(
                        new SimpleGrantedAuthority("ROLE_" + savedUser.getRole().name())
                )
        );

        // GENERATE TOKEN
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(
                token,
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        // AUTHENTICATE USER
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // FETCH USER
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        // BUILD USER DETAILS FOR JWT
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                )
        );

        // GENERATE TOKEN
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }
}