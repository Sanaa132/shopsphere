package com.example.shopsphere.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponse {

    private Long cartId;

    private List<CartItemResponse> items;

    private BigDecimal totalPrice;
}