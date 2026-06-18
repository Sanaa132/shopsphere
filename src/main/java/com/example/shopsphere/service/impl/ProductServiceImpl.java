package com.example.shopsphere.service.impl;

import com.example.shopsphere.dto.request.ProductRequest;
import com.example.shopsphere.dto.response.ProductResponse;
import com.example.shopsphere.entity.Category;
import com.example.shopsphere.entity.Product;
import com.example.shopsphere.exception.BadRequestException;
import com.example.shopsphere.exception.ResourceNotFoundException;
import com.example.shopsphere.repository.CategoryRepository;
import com.example.shopsphere.repository.ProductRepository;
import com.example.shopsphere.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // CREATE PRODUCT
    @Override
    public ProductResponse createProduct(ProductRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found")
                );

        // VALIDATION
        if (request.getDiscountedPrice() != null &&
                request.getDiscountedPrice().compareTo(request.getPrice()) > 0) {

            throw new BadRequestException(
                    "Discounted price cannot be greater than original price"
            );
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setDiscountedPrice(request.getDiscountedPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        // Ensure starting numbers are initialized as 0 rather than null on creation
        product.setViewCount(0L);
        product.setPurchaseCount(0L);

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    // GET ALL PRODUCTS (N+1 PROBLEM FIXED)
    @Override
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAllWithCategory() // <-- Swapped to your custom JOIN FETCH query
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // GET PRODUCT BY ID (CONCURRENCY FIXED)
    @Override
    @Transactional
    public ProductResponse getProductById(Long id) {
        // 1. Atomic DB increment first - avoids dirty reads and race conditions
        productRepository.incrementViewCount(id);

        // 2. Fetch the newly updated record state to display accurately to user
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found")
                );

        return mapToResponse(product);
    }

    // UPDATE PRODUCT
    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found")
                );

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found")
                );

        if (request.getDiscountedPrice() != null &&
                request.getDiscountedPrice().compareTo(request.getPrice()) > 0) {

            throw new BadRequestException(
                    "Discounted price cannot be greater than original price"
            );
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setDiscountedPrice(request.getDiscountedPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        return mapToResponse(updatedProduct);
    }

    // DELETE PRODUCT
    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found")
                );

        productRepository.delete(product);
    }

    // MAPPER
    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setDiscountedPrice(product.getDiscountedPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setImageUrl(product.getImageUrl());

        response.setRating(product.getRating());

        // Null-safe fallbacks for mapper mapping
        response.setViewCount(product.getViewCount() != null ? product.getViewCount() : 0L);
        response.setPurchaseCount(product.getPurchaseCount() != null ? product.getPurchaseCount() : 0L);

        response.setCategoryName(
                product.getCategory() != null
                        ? product.getCategory().getName()
                        : null
        );

        return response;
    }

    @Override
    public Page<ProductResponse> searchProducts(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository
                .findByNameContainingIgnoreCase(query, pageable);

        return products.map(this::mapToResponse);
    }
}