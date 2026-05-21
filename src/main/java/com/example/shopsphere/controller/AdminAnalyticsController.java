package com.example.shopsphere.controller;

import com.example.shopsphere.service.AnalyticsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminAnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/analytics")
    public Map<String, Object> getAnalytics() {

        return analyticsService.getAnalytics();
    }
}