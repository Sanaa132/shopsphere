package com.example.shopsphere.controller;

import com.example.shopsphere.dto.request.OrderRequest;
import com.example.shopsphere.dto.response.OrderResponse;
import com.example.shopsphere.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // PLACE ORDER
    @PostMapping("/place")
    public OrderResponse placeOrder(@RequestBody OrderRequest request) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return orderService.placeOrder(email, request);
    }

    // ORDER HISTORY
    @GetMapping("/history")
    public List<OrderResponse> getOrderHistory() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return orderService.getOrderHistory(email);
    }
}