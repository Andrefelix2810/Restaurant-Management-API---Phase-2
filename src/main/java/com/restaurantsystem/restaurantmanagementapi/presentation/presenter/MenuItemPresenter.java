package com.restaurantsystem.restaurantmanagementapi.presentation.presenter;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.MenuItem;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.MenuItemResponse;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.MenuItemRestaurantResponse;

public final class MenuItemPresenter {

    private MenuItemPresenter() {
    }

    public static MenuItemResponse toResponse(MenuItem menuItem) {
        Restaurant restaurant = menuItem.getRestaurant();
        return new MenuItemResponse(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.isAvailableOnlyInRestaurant(),
                menuItem.getPhotoPath(),
                new MenuItemRestaurantResponse(restaurant.getId(), restaurant.getName())
        );
    }
}
