package com.example.shopsphere.repository;

import com.example.shopsphere.entity.Wishlist;
import com.example.shopsphere.entity.User;
import com.example.shopsphere.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findByUserAndProduct(User user, Product product);

    List<Wishlist> findByUser(User user);
}