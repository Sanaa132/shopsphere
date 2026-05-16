package com.example.shopsphere.service;

import com.example.shopsphere.dto.response.ProductResponse;

import java.util.List;

public interface WishlistService {

    void addToWishlist(String email, Long productId);

    void removeFromWishlist(String email, Long productId);

    List<ProductResponse> getWishlist(String email);
}