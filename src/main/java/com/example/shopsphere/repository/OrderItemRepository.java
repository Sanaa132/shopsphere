package com.example.shopsphere.repository;

import com.example.shopsphere.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
        SELECT c.name, SUM(oi.quantity)
        FROM OrderItem oi
        JOIN oi.product p
        JOIN p.category c
        GROUP BY c.name
    """)
    List<Object[]> findCategoryPurchaseTotals();
}