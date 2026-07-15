package com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.mapper;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.MenuItem;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.entity.JpaMenuItemEntity;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.entity.JpaRestaurantEntity;

public final class MenuItemPersistenceMapper {

    private MenuItemPersistenceMapper() {
    }

    public static MenuItem toDomain(JpaMenuItemEntity entity) {
        return MenuItem.restore(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.isAvailableOnlyInRestaurant(),
                entity.getPhotoPath(),
                RestaurantPersistenceMapper.toDomain(entity.getRestaurant())
        );
    }

    public static JpaMenuItemEntity toEntity(MenuItem menuItem, JpaRestaurantEntity restaurant) {
        return new JpaMenuItemEntity(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.isAvailableOnlyInRestaurant(),
                menuItem.getPhotoPath(),
                restaurant
        );
    }
}
