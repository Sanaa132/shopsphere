package com.example.shopsphere.service.impl;

import com.example.shopsphere.dto.request.OrderRequest;
import com.example.shopsphere.dto.response.OrderItemResponse;
import com.example.shopsphere.dto.response.OrderResponse;
import com.example.shopsphere.entity.*;
import com.example.shopsphere.exception.ResourceNotFoundException;
import com.example.shopsphere.repository.*;
import com.example.shopsphere.exception.BadRequestException;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopsphere.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired private UserRepository userRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;

    @Transactional
    @Override
    public OrderResponse placeOrder(String email, OrderRequest request) {

        // 1. Get user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 2. Get cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 3. Process each cart item
        for (Long cartItemId : request.getCartItemIds()) {

            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

            // ownership check
            if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
                throw new ResourceNotFoundException("Unauthorized cart item access");
            }

            Product product = cartItem.getProduct();

            // stock check
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new  BadRequestException("Insufficient stock for " + product.getName());
            }

            // reduce stock
            product.setStockQuantity(
                    product.getStockQuantity() - cartItem.getQuantity()
            );

            product.setPurchaseCount(
                    product.getPurchaseCount() + cartItem.getQuantity()
            );

            productRepository.save(product);

            // create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getDiscountedPrice());

            BigDecimal subtotal = product.getDiscountedPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            totalAmount = totalAmount.add(subtotal);

            orderItems.add(orderItem);

            // remove from cart
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        }

        // 4. Create order
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PLACED);
        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        // link back order -> orderItems
        for (OrderItem item : orderItems) {
            item.setOrder(order);
        }

        OrderEntity savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getOrderHistory(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<OrderEntity> orders = orderRepository.findByUser(user);

        List<OrderResponse> responseList = new ArrayList<>();

        for (OrderEntity order : orders) {
            responseList.add(mapToResponse(order));
        }

        return responseList;
    }

    // ---------------- MAPPERS ----------------

    private OrderResponse mapToResponse(OrderEntity order) {

        OrderResponse response = new OrderResponse();

        response.setOrderId(order.getId());
        response.setOrderDate(order.getOrderDate());
        response.setStatus(order.getStatus());
        response.setTotalAmount(order.getTotalAmount());

        List<OrderItemResponse> items = new ArrayList<>();

        for (OrderItem item : order.getOrderItems()) {

            OrderItemResponse r = new OrderItemResponse();
            r.setProductId(item.getProduct().getId());
            r.setProductName(item.getProduct().getName());
            r.setQuantity(item.getQuantity());
            r.setPrice(item.getPrice());

            BigDecimal subtotal = item.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            r.setSubtotal(subtotal);

            items.add(r);
        }

        response.setItems(items);

        return response;
    }
}