package com.example.shopsphere.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {

    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private BigDecimal discountedPrice;

    private Integer stockQuantity;

    private String imageUrl;

    private Double rating;

    private Long viewCount;

    private Long purchaseCount;

    private String categoryName;
}