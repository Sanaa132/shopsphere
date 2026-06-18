package com.example.shopsphere.dto.response;

import com.example.shopsphere.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponse {

    private Long orderId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String imageUrl;
    private String orderNumber;

    private List<com.example.shopsphere.dto.response.OrderItemResponse> items;
}