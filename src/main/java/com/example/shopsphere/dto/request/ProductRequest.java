package com.example.shopsphere.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    private String name;

    private String description;

    private BigDecimal price;

    private BigDecimal discountedPrice;

    private Integer stockQuantity;

    private String imageUrl;

    private Long categoryId;
}