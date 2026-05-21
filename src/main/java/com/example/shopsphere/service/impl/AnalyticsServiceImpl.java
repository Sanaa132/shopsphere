package com.example.shopsphere.service.impl;

import com.example.shopsphere.dto.response.CategoryPurchaseResponse;
import com.example.shopsphere.dto.response.ProductViewResponse;
import com.example.shopsphere.entity.OrderItem;
import com.example.shopsphere.entity.Product;
import com.example.shopsphere.repository.OrderItemRepository;
import com.example.shopsphere.repository.ProductRepository;
import com.example.shopsphere.service.AnalyticsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Map<String, Object> getAnalytics() {

        // =========================
        // CATEGORY PURCHASE DATA
        // =========================

        List<OrderItem> orderItems =
                orderItemRepository.findAll();

        Map<String, Long> categoryMap =
                new HashMap<>();

        for (OrderItem item : orderItems) {

            Product product = item.getProduct();

            if (product == null ||
                    product.getCategory() == null) {
                continue;
            }

            String category =
                    product.getCategory().getName();

            categoryMap.put(
                    category,
                    categoryMap.getOrDefault(category, 0L)
                            + item.getQuantity()
            );
        }

        List<CategoryPurchaseResponse>
                categoryPurchaseData = new ArrayList<>();

        for (Map.Entry<String, Long> entry
                : categoryMap.entrySet()) {

            categoryPurchaseData.add(
                    new CategoryPurchaseResponse(
                            entry.getKey(),
                            entry.getValue()
                    )
            );
        }

        // =========================
        // PRODUCT VIEW DATA
        // =========================

        List<Product> products =
                productRepository.findAll();

        products.sort((a, b) ->
                Long.compare(
                        b.getViewCount(),
                        a.getViewCount()
                )
        );

        List<ProductViewResponse>
                productViewData = new ArrayList<>();

        int limit = Math.min(products.size(), 5);

        for (int i = 0; i < limit; i++) {

            Product p = products.get(i);

            productViewData.add(
                    new ProductViewResponse(
                            p.getName(),
                            p.getViewCount()
                    )
            );
        }

        // =========================
        // FINAL RESPONSE
        // =========================

        Map<String, Object> result =
                new HashMap<>();

        result.put(
                "categoryPurchaseData",
                categoryPurchaseData
        );

        result.put(
                "productViewData",
                productViewData
        );

        return result;
    }
}