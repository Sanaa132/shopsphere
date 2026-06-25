package com.example.shopsphere.service.impl;

import com.example.shopsphere.dto.response.CartItemResponse;
import com.example.shopsphere.dto.response.CartResponse;
import com.example.shopsphere.entity.Cart;
import com.example.shopsphere.entity.CartItem;
import com.example.shopsphere.entity.Product;
import com.example.shopsphere.entity.User;
import com.example.shopsphere.exception.ResourceNotFoundException;
import com.example.shopsphere.repository.CartItemRepository;
import com.example.shopsphere.repository.CartRepository;
import com.example.shopsphere.repository.ProductRepository;
import com.example.shopsphere.repository.UserRepository;
import com.example.shopsphere.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    // GET CART
    @Transactional
    @Override
    public CartResponse getCartByUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setCartItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });
        cart.getCartItems().size();
        return mapToCartResponse(cart);
    }

    // ADD TO CART
    @Transactional
    @Override
    public CartResponse addToCart(String email, Long productId, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found")
                );

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setCartItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });

        cart.getCartItems().size();

        // CHECK IF PRODUCT ALREADY EXISTS
        CartItem existingItem = null;

        for (CartItem item : cart.getCartItems()) {
            if (item.getProduct().getId().equals(productId)) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);

            cartItemRepository.save(newItem);
            cart.getCartItems().add(newItem);
        }

        return mapToCartResponse(cart);
    }

    // UPDATE QUANTITY
    @Transactional
    @Override
    public CartResponse updateQuantity(String email, Long cartItemId, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart item not found")
                );

        // OWNERSHIP CHECK
        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Unauthorized access to cart item");
        }

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return mapToCartResponse(cartItem.getCart());
    }

    // REMOVE ITEM
    @Transactional
    @Override
    public void removeFromCart(String email, Long cartItemId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart item not found")
                );

        // OWNERSHIP CHECK
        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new ResourceNotFoundException("Unauthorized access");
        }

        Cart cart = cartItem.getCart();

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        cartRepository.save(cart);
    }

    // MAP CART → RESPONSE
    private CartResponse mapToCartResponse(Cart cart) {

        CartResponse response = new CartResponse();

        response.setCartId(cart.getId());

        List<CartItemResponse> items = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        if (cart.getCartItems() != null) {
            for (CartItem item : cart.getCartItems()) {
                CartItemResponse itemResponse = mapToCartItemResponse(item);
                items.add(itemResponse);
                totalPrice = totalPrice.add(itemResponse.getSubtotal());
            }
        }

        response.setItems(items);
        response.setTotalPrice(totalPrice);

        return response;
    }

    // MAP ITEM → RESPONSE
    private CartItemResponse mapToCartItemResponse(CartItem cartItem) {

        CartItemResponse response = new CartItemResponse();

        response.setCartItemId(cartItem.getId());
        response.setProductId(cartItem.getProduct().getId());
        response.setProductName(cartItem.getProduct().getName());
        response.setImageUrl(cartItem.getProduct().getImageUrl());

        response.setPrice(cartItem.getProduct().getDiscountedPrice());
        response.setQuantity(cartItem.getQuantity());

        BigDecimal subtotal =
                cartItem.getProduct().getDiscountedPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        response.setSubtotal(subtotal);

        return response;
    }
}