package com.example.shopsphere.dto.response;

public class ProductViewResponse {

    private String productName;

    private Long viewCount;

    public ProductViewResponse(
            String productName,
            Long viewCount
    ) {
        this.productName = productName;
        this.viewCount = viewCount;
    }

    public String getProductName() {
        return productName;
    }

    public Long getViewCount() {
        return viewCount;
    }
}