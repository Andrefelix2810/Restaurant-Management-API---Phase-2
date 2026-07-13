package com.restaurantsystem.restaurantmanagementapi.mapper;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.RestaurantCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.RestaurantUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.RestaurantOwnerResponse;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.RestaurantResponse;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {

    private final UserTypeMapper userTypeMapper;

    public RestaurantMapper(UserTypeMapper userTypeMapper) {
        this.userTypeMapper = userTypeMapper;
    }

    public Restaurant toEntity(RestaurantCreateRequest request, User owner) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setCuisineType(request.getCuisineType());
        restaurant.setOpeningHours(request.getOpeningHours());
        restaurant.setOwner(owner);
        return restaurant;
    }

    public void updateEntity(Restaurant restaurant, RestaurantUpdateRequest request, User owner) {
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setCuisineType(request.getCuisineType());
        restaurant.setOpeningHours(request.getOpeningHours());
        restaurant.setOwner(owner);
    }

    public RestaurantResponse toResponse(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getCuisineType(),
                restaurant.getOpeningHours(),
                toOwnerResponse(restaurant.getOwner())
        );
    }

    private RestaurantOwnerResponse toOwnerResponse(User owner) {
        if (owner == null) {
            return null;
        }

        return new RestaurantOwnerResponse(
                owner.getId(),
                owner.getName(),
                owner.getEmail(),
                userTypeMapper.toResponse(owner.getUserType())
        );
    }
}
