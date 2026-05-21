package com.example.shopsphere.config;

import com.example.shopsphere.entity.Category;
import com.example.shopsphere.entity.Product;
import com.example.shopsphere.repository.CategoryRepository;
import com.example.shopsphere.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {

        // STOP if products already exist
        if (productRepository.count() > 0) {
            System.out.println("✅ Products already exist");
            return;
        }

        // =========================
        // ELECTRONICS CATEGORY
        // =========================

        Category electronicsCategory = new Category();
        electronicsCategory.setName("Electronics");

        Category electronics =
                categoryRepository.findByName("Electronics")
                        .orElseGet(() ->
                                categoryRepository.save(electronicsCategory)
                        );

        // =========================
        // FASHION CATEGORY
        // =========================

        Category fashionCategory = new Category();
        fashionCategory.setName("Fashion");

        Category fashion =
                categoryRepository.findByName("Fashion")
                        .orElseGet(() ->
                                categoryRepository.save(fashionCategory)
                        );

        // =========================
        // APPLIANCES CATEGORY
        // =========================

        Category appliancesCategory = new Category();
        appliancesCategory.setName("Home Appliances");

        Category appliances =
                categoryRepository.findByName("Home Appliances")
                        .orElseGet(() ->
                                categoryRepository.save(appliancesCategory)
                        );

        // =========================
        // BOOKS CATEGORY
        // =========================

        Category booksCategory = new Category();
        booksCategory.setName("Books");

        Category books =
                categoryRepository.findByName("Books")
                        .orElseGet(() ->
                                categoryRepository.save(booksCategory)
                        );

        // =========================
        // SPORTS CATEGORY
        // =========================

        Category sportsCategory = new Category();
        sportsCategory.setName("Sports");

        Category sports =
                categoryRepository.findByName("Sports")
                        .orElseGet(() ->
                                categoryRepository.save(sportsCategory)
                        );

        // =========================
        // PRODUCTS
        // =========================

        Product iphone = new Product();
        iphone.setName("iPhone 15");
        iphone.setDescription("Latest Apple smartphone");
        iphone.setPrice(new BigDecimal("85000"));
        iphone.setDiscountedPrice(new BigDecimal("79999"));
        iphone.setStockQuantity(25);
        iphone.setRating(4.8);
        iphone.setViewCount(120L);
        iphone.setPurchaseCount(40L);
        iphone.setImageUrl("https://placehold.co/400x300/orange/white?text=iPhone+15");
        iphone.setCategory(electronics);

        Product samsung = new Product();
        samsung.setName("Samsung Galaxy S24");
        samsung.setDescription("Flagship Samsung phone");
        samsung.setPrice(new BigDecimal("78000"));
        samsung.setDiscountedPrice(new BigDecimal("72000"));
        samsung.setStockQuantity(40);
        samsung.setRating(4.7);
        samsung.setViewCount(110L);
        samsung.setPurchaseCount(35L);
        samsung.setImageUrl("https://placehold.co/400x300/black/white?text=Galaxy+S24");
        samsung.setCategory(electronics);

        Product sony = new Product();
        sony.setName("Sony Headphones");
        sony.setDescription("Noise cancelling headphones");
        sony.setPrice(new BigDecimal("15000"));
        sony.setDiscountedPrice(new BigDecimal("12000"));
        sony.setStockQuantity(18);
        sony.setRating(4.5);
        sony.setViewCount(95L);
        sony.setPurchaseCount(22L);
        sony.setImageUrl("https://placehold.co/400x300/navy/white?text=Sony+Headphones");
        sony.setCategory(electronics);

        Product nike = new Product();
        nike.setName("Nike Running Shoes");
        nike.setDescription("Comfort sports shoes");
        nike.setPrice(new BigDecimal("7000"));
        nike.setDiscountedPrice(new BigDecimal("5999"));
        nike.setStockQuantity(45);
        nike.setRating(4.4);
        nike.setViewCount(80L);
        nike.setPurchaseCount(15L);
        nike.setImageUrl("https://placehold.co/400x300/red/white?text=Nike+Shoes");
        nike.setCategory(sports);

        Product hoodie = new Product();
        hoodie.setName("Adidas Hoodie");
        hoodie.setDescription("Premium cotton hoodie");
        hoodie.setPrice(new BigDecimal("4500"));
        hoodie.setDiscountedPrice(new BigDecimal("3999"));
        hoodie.setStockQuantity(60);
        hoodie.setRating(4.2);
        hoodie.setViewCount(50L);
        hoodie.setPurchaseCount(10L);
        hoodie.setImageUrl("https://placehold.co/400x300/gray/white?text=Adidas+Hoodie");
        hoodie.setCategory(fashion);

        Product washingMachine = new Product();
        washingMachine.setName("LG Washing Machine");
        washingMachine.setDescription("Front load washing machine");
        washingMachine.setPrice(new BigDecimal("45000"));
        washingMachine.setDiscountedPrice(new BigDecimal("42000"));
        washingMachine.setStockQuantity(12);
        washingMachine.setRating(4.6);
        washingMachine.setViewCount(40L);
        washingMachine.setPurchaseCount(8L);
        washingMachine.setImageUrl("https://placehold.co/400x300/blue/white?text=LG+Washing+Machine");
        washingMachine.setCategory(appliances);

        Product atomicHabits = new Product();
        atomicHabits.setName("Atomic Habits");
        atomicHabits.setDescription("Self improvement book");
        atomicHabits.setPrice(new BigDecimal("799"));
        atomicHabits.setDiscountedPrice(new BigDecimal("599"));
        atomicHabits.setStockQuantity(120);
        atomicHabits.setRating(4.9);
        atomicHabits.setViewCount(150L);
        atomicHabits.setPurchaseCount(55L);
        atomicHabits.setImageUrl("https://placehold.co/400x300/gold/black?text=Atomic+Habits");
        atomicHabits.setCategory(books);

        Product football = new Product();
        football.setName("Football");
        football.setDescription("Professional football");
        football.setPrice(new BigDecimal("1200"));
        football.setDiscountedPrice(new BigDecimal("999"));
        football.setStockQuantity(35);
        football.setRating(4.3);
        football.setViewCount(45L);
        football.setPurchaseCount(11L);
        football.setImageUrl("https://placehold.co/400x300/green/white?text=Football");
        football.setCategory(sports);

        Product laptop = new Product();
        laptop.setName("Dell Laptop");
        laptop.setDescription("Gaming laptop");
        laptop.setPrice(new BigDecimal("95000"));
        laptop.setDiscountedPrice(new BigDecimal("89999"));
        laptop.setStockQuantity(15);
        laptop.setRating(4.7);
        laptop.setViewCount(140L);
        laptop.setPurchaseCount(38L);
        laptop.setImageUrl("https://placehold.co/400x300/black/orange?text=Dell+Laptop");
        laptop.setCategory(electronics);

        Product tshirt = new Product();
        tshirt.setName("Puma T-Shirt");
        tshirt.setDescription("Casual wear t-shirt");
        tshirt.setPrice(new BigDecimal("1999"));
        tshirt.setDiscountedPrice(new BigDecimal("1499"));
        tshirt.setStockQuantity(70);
        tshirt.setRating(4.1);
        tshirt.setViewCount(35L);
        tshirt.setPurchaseCount(6L);
        tshirt.setImageUrl("https://placehold.co/400x300/orange/white?text=Puma+TShirt");
        tshirt.setCategory(fashion);

        productRepository.saveAll(
                List.of(
                        iphone,
                        samsung,
                        sony,
                        nike,
                        hoodie,
                        washingMachine,
                        atomicHabits,
                        football,
                        laptop,
                        tshirt
                )
        );

        System.out.println("✅ Sample products inserted successfully!");
    }
}