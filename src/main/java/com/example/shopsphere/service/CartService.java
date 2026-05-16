package com.example.shopsphere.service;

import com.example.shopsphere.dto.response.CartResponse;

public interface CartService {

    CartResponse getCartByUser(String email);

    CartResponse addToCart(
            String email,
            Long productId,
            int quantity
    );

    CartResponse updateQuantity(
            String email,
            Long cartItemId,
            int quantity
    );

    void removeFromCart(
            String email,
            Long cartItemId
    );
}