package com.example.shopsphere.repository;

import com.example.shopsphere.entity.Cart;
import com.example.shopsphere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
}