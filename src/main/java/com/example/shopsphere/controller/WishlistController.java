package com.example.shopsphere.controller;

import com.example.shopsphere.dto.response.ProductResponse;
import com.example.shopsphere.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/add")
    public String addToWishlist(@RequestParam Long productId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        wishlistService.addToWishlist(email, productId);
        return "Added to wishlist";
    }

    @DeleteMapping("/remove")
    public String removeFromWishlist(@RequestParam Long productId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        wishlistService.removeFromWishlist(email, productId);
        return "Removed from wishlist";
    }

    @GetMapping
    public List<ProductResponse> getWishlist() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return wishlistService.getWishlist(email);
}}