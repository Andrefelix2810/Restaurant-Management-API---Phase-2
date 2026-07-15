package com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.mapper;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.entity.JpaRestaurantEntity;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.entity.JpaUserEntity;

public final class RestaurantPersistenceMapper {

    private RestaurantPersistenceMapper() {
    }

    public static Restaurant toDomain(JpaRestaurantEntity entity) {
        return Restaurant.restore(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getCuisineType(),
                entity.getOpeningHours(),
                UserPersistenceMapper.toDomain(entity.getOwner())
        );
    }

    public static JpaRestaurantEntity toEntity(Restaurant restaurant, JpaUserEntity owner) {
        return new JpaRestaurantEntity(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getCuisineType(),
                restaurant.getOpeningHours(),
                owner
        );
    }
}
