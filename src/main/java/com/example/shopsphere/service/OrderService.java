package com.example.shopsphere.service;

import com.example.shopsphere.dto.request.OrderRequest;
import com.example.shopsphere.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(String email, OrderRequest request);

    List<OrderResponse> getOrderHistory(String email);
}