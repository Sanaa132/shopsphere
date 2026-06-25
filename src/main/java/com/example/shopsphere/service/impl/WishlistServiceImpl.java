package com.example.shopsphere.service.impl;

import com.example.shopsphere.dto.response.ProductResponse;
import com.example.shopsphere.entity.*;
import com.example.shopsphere.exception.ResourceNotFoundException;
import com.example.shopsphere.repository.*;
import com.example.shopsphere.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistServiceImpl implements WishlistService {

    @Autowired private WishlistRepository wishlistRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;

    @Transactional
    @Override
    public void addToWishlist(String email, Long productId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // prevent duplicates
        boolean exists = wishlistRepository.findByUserAndProduct(user, product).isPresent();

        if (exists) {
            return; // silently ignore OR you can throw BadRequestException
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);

        wishlistRepository.save(wishlist);
    }

    @Transactional
    @Override
    public void removeFromWishlist(String email, Long productId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Wishlist wishlist = wishlistRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist item not found"));

        wishlistRepository.delete(wishlist);
    }

    @Transactional
    @Override
    public List<ProductResponse> getWishlist(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return wishlistRepository.findByUser(user)
                .stream()
                .map(w -> mapToProductResponse(w.getProduct()))
                .collect(Collectors.toList());
    }

    // reuse same style as ProductService
    private ProductResponse mapToProductResponse(Product product) {

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
                product.getCategory() != null ? product.getCategory().getName() : null
        );

        return response;
    }
}