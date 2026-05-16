package com.example.shopsphere.repository;

import com.example.shopsphere.entity.OtpToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {

}