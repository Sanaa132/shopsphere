package com.example.shopsphere.controller;

import com.example.shopsphere.dto.request.ProductRequest;
import com.example.shopsphere.dto.response.ProductResponse;
import com.example.shopsphere.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // GET ALL PRODUCTS (PUBLIC)
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // GET PRODUCT BY ID (PUBLIC)
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // SEARCH PRODUCTS (PUBLIC)
    @GetMapping("/search")
    public Page<ProductResponse> searchProducts(
            @RequestParam String query,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return productService.searchProducts(query, page, size);
    }
}