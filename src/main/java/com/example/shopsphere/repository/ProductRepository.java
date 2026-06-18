package com.example.shopsphere.repository;

import com.example.shopsphere.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Pagination-supported text search
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Concurrency Fix: Atomic database-level increment to eliminate race conditions
    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.viewCount = COALESCE(p.viewCount, 0) + 1 WHERE p.id = :id")
    void incrementViewCount(@Param("id") Long id);

    // Performance Fix: Eagerly fetches categories in a single query to eliminate N+1 overhead
    @Query("SELECT p FROM Product p JOIN FETCH p.category")
    List<Product> findAllWithCategory();
}