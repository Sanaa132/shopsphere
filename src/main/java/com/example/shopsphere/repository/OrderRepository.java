package com.example.shopsphere.repository;

import com.example.shopsphere.entity.OrderEntity;
import com.example.shopsphere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByUser(User user);
}