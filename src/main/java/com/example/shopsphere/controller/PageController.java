package com.example.shopsphere.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login-page")
    public String login() {
        return "login";
    }

    @GetMapping("/register-page")
    public String register() {
        return "register";
    }

    @GetMapping("/cart-page")
    public String cart() {
        return "cart";
    }

    @GetMapping("/orders-page")
    public String orders() {
        return "orders";
    }

    @GetMapping("/wishlist-page")
    public String wishlist() {
        return "wishlist";
    }

    @GetMapping("/product/{id}")
    public String productDetailPage() {
        return "product-detail";
    }

    @GetMapping("/admin-page")
    public String adminPage() {
        return "admin";
    }
}