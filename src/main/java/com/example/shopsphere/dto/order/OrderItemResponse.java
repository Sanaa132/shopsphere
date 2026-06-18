package com.example.shopsphere.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemResponse {

    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
    private String imageUrl;
}