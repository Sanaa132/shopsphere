package com.example.shopsphere.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponse {

    private Long cartItemId;

    private Long productId;

    private String productName;

    private String imageUrl;

    private BigDecimal price;

    private int quantity;

    private BigDecimal subtotal;
}