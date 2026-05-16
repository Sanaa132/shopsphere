package com.example.shopsphere.controller;

import com.example.shopsphere.dto.response.CartResponse;
import com.example.shopsphere.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // GET USER CART
    @GetMapping
    public CartResponse getCart() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return cartService.getCartByUser(email);
    }

    // ADD TO CART
    @PostMapping("/add")
    public CartResponse addToCart(
            @RequestParam Long productId,
            @RequestParam int quantity
    ) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return cartService.addToCart(
                email,
                productId,
                quantity
        );
    }

    // UPDATE QUANTITY
    @PutMapping("/update/{cartItemId}")
    public CartResponse updateQuantity(
            @PathVariable Long cartItemId,
            @RequestParam int quantity
    ) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return cartService.updateQuantity(
                email,
                cartItemId,
                quantity
        );
    }

    // REMOVE ITEM
    @DeleteMapping("/remove/{cartItemId}")
    public void removeFromCart(
            @PathVariable Long cartItemId
    ) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        cartService.removeFromCart(
                email,
                cartItemId
        );
    }
}