package com.restaurantsystem.restaurantmanagementapi.domain.entity;

import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;

import java.math.BigDecimal;

public class MenuItem {

    private final Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean availableOnlyInRestaurant;
    private String photoPath;
    private Restaurant restaurant;

    private MenuItem(
            Long id,
            String name,
            String description,
            BigDecimal price,
            boolean availableOnlyInRestaurant,
            String photoPath,
            Restaurant restaurant
    ) {
        validate(name, price, restaurant);
        this.id = id;
        this.name = name.trim();
        this.description = description;
        this.price = price;
        this.availableOnlyInRestaurant = availableOnlyInRestaurant;
        this.photoPath = photoPath;
        this.restaurant = restaurant;
    }

    public static MenuItem create(
            String name,
            String description,
            BigDecimal price,
            boolean availableOnlyInRestaurant,
            String photoPath,
            Restaurant restaurant
    ) {
        return new MenuItem(
                null,
                name,
                description,
                price,
                availableOnlyInRestaurant,
                photoPath,
                restaurant
        );
    }

    public static MenuItem restore(
            Long id,
            String name,
            String description,
            BigDecimal price,
            boolean availableOnlyInRestaurant,
            String photoPath,
            Restaurant restaurant
    ) {
        return new MenuItem(
                id,
                name,
                description,
                price,
                availableOnlyInRestaurant,
                photoPath,
                restaurant
        );
    }

    public void update(
            String name,
            String description,
            BigDecimal price,
            boolean availableOnlyInRestaurant,
            String photoPath,
            Restaurant restaurant
    ) {
        validate(name, price, restaurant);
        this.name = name.trim();
        this.description = description;
        this.price = price;
        this.availableOnlyInRestaurant = availableOnlyInRestaurant;
        this.photoPath = photoPath;
        this.restaurant = restaurant;
    }

    private static void validate(String name, BigDecimal price, Restaurant restaurant) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException("Name is required");
        }
        if (price == null) {
            throw new BusinessException("Price is required");
        }
        if (price.signum() <= 0) {
            throw new BusinessException("Price must be greater than zero");
        }
        if (restaurant == null) {
            throw new BusinessException("Restaurant is required");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isAvailableOnlyInRestaurant() {
        return availableOnlyInRestaurant;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}
