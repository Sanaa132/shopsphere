package com.example.shopsphere.dto.response;

public class CategoryPurchaseResponse {

    private String categoryName;

    private Long totalPurchases;

    public CategoryPurchaseResponse(
            String categoryName,
            Long totalPurchases
    ) {
        this.categoryName = categoryName;
        this.totalPurchases = totalPurchases;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Long getTotalPurchases() {
        return totalPurchases;
    }
}