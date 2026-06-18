package com.example.shopsphere.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // 1. PUBLIC ASSETS & HTML VIEWS (Permit All)
                        // Allows the page layout to load so your JS can handle smooth local redirects
                        .requestMatchers(
                                "/",
                                "/login-page",
                                "/register-page",
                                "/admin-page",
                                "/cart-page",     // Moved here to prevent raw browser 403s
                                "/wishlist-page", // Moved here to prevent raw browser 403s
                                "/orders-page",   // Moved here to prevent raw browser 403s
                                "/product/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico",
                                "/auth/**"
                        ).permitAll()

                        // 2. PUBLIC API ENDPOINTS
                        .requestMatchers("/api/products/**").permitAll()

                        // 3. SECURED USER API ROUTES (Requires JWT Authentication)
                        .requestMatchers("/api/cart/**").authenticated()
                        .requestMatchers("/api/wishlist/**").authenticated()
                        .requestMatchers("/api/orders/**").authenticated()

                        // 4. SECURED ADMIN API ROUTES (Requires JWT Authentication + ADMIN Role)
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 5. CATCH-ALL SAFETY NET
                        .anyRequest().authenticated()
                );

        // Inject your JWT filter to process tokens on incoming requests
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}