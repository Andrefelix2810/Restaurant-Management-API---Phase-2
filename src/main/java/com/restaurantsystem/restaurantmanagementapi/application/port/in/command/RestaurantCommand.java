package com.restaurantsystem.restaurantmanagementapi.application.port.in.command;

public record RestaurantCommand(
        String name,
        String address,
        String cuisineType,
        String openingHours,
        Long ownerId
) {
}
