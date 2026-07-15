package com.restaurantsystem.restaurantmanagementapi.presentation.presenter;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.RestaurantOwnerResponse;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.RestaurantResponse;

public final class RestaurantPresenter {

    private RestaurantPresenter() {
    }

    public static RestaurantResponse toResponse(Restaurant restaurant) {
        User owner = restaurant.getOwner();
        RestaurantOwnerResponse ownerResponse = new RestaurantOwnerResponse(
                owner.getId(),
                owner.getName(),
                owner.getEmail(),
                UserTypePresenter.toResponse(owner.getUserType())
        );
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getCuisineType(),
                restaurant.getOpeningHours(),
                ownerResponse
        );
    }
}
