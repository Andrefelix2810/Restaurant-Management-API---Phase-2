package com.restaurantsystem.restaurantmanagementapi.domain.entity;

import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;

public class Restaurant {

    private final Long id;
    private String name;
    private String address;
    private String cuisineType;
    private String openingHours;
    private User owner;

    private Restaurant(
            Long id,
            String name,
            String address,
            String cuisineType,
            String openingHours,
            User owner
    ) {
        validate(name, address, cuisineType, openingHours, owner);
        this.id = id;
        this.name = name.trim();
        this.address = address.trim();
        this.cuisineType = cuisineType.trim();
        this.openingHours = openingHours.trim();
        this.owner = owner;
    }

    public static Restaurant create(
            String name,
            String address,
            String cuisineType,
            String openingHours,
            User owner
    ) {
        return new Restaurant(null, name, address, cuisineType, openingHours, owner);
    }

    public static Restaurant restore(
            Long id,
            String name,
            String address,
            String cuisineType,
            String openingHours,
            User owner
    ) {
        return new Restaurant(id, name, address, cuisineType, openingHours, owner);
    }

    public void update(
            String name,
            String address,
            String cuisineType,
            String openingHours,
            User owner
    ) {
        validate(name, address, cuisineType, openingHours, owner);
        this.name = name.trim();
        this.address = address.trim();
        this.cuisineType = cuisineType.trim();
        this.openingHours = openingHours.trim();
        this.owner = owner;
    }

    private static void validate(
            String name,
            String address,
            String cuisineType,
            String openingHours,
            User owner
    ) {
        requireText(name, "Name is required");
        requireText(address, "Address is required");
        requireText(cuisineType, "Cuisine type is required");
        requireText(openingHours, "Opening hours is required");
        if (owner == null) {
            throw new BusinessException("Owner is required");
        }
    }

    private static void requireText(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException(message);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public User getOwner() {
        return owner;
    }
}
