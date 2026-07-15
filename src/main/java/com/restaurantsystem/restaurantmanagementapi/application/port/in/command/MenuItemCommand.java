package com.restaurantsystem.restaurantmanagementapi.application.port.in.command;

import java.math.BigDecimal;

public record MenuItemCommand(
        String name,
        String description,
        BigDecimal price,
        boolean availableOnlyInRestaurant,
        String photoPath,
        Long restaurantId
) {
}
