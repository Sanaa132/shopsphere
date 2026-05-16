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
import org.springframework.stereotype.Service;

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

        // VALIDATION (correct exception type)
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

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    // GET ALL PRODUCTS
    @Override
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // GET PRODUCT BY ID
    @Override
    public ProductResponse getProductById(Long id) {

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

        // VALIDATION FIXED
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
        response.setViewCount(product.getViewCount());
        response.setPurchaseCount(product.getPurchaseCount());

        response.setCategoryName(
                product.getCategory() != null
                        ? product.getCategory().getName()
                        : null
        );

        return response;
    }
}