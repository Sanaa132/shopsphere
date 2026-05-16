package com.example.shopsphere.repository;

import com.example.shopsphere.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}