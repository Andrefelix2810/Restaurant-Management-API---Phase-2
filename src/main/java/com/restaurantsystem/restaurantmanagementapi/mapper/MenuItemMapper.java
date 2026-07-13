package com.restaurantsystem.restaurantmanagementapi.mapper;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.MenuItem;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.MenuItemCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.MenuItemUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.MenuItemResponse;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.MenuItemRestaurantResponse;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {

    public MenuItem toEntity(MenuItemCreateRequest request, Restaurant restaurant) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setAvailableOnlyInRestaurant(request.isAvailableOnlyInRestaurant());
        menuItem.setPhotoPath(request.getPhotoPath());
        menuItem.setRestaurant(restaurant);
        return menuItem;
    }

    public void updateEntity(MenuItem menuItem, MenuItemUpdateRequest request, Restaurant restaurant) {
        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setAvailableOnlyInRestaurant(request.isAvailableOnlyInRestaurant());
        menuItem.setPhotoPath(request.getPhotoPath());
        menuItem.setRestaurant(restaurant);
    }

    public MenuItemResponse toResponse(MenuItem menuItem) {
        Restaurant restaurant = menuItem.getRestaurant();
        MenuItemRestaurantResponse restaurantResponse = new MenuItemRestaurantResponse(
                restaurant.getId(),
                restaurant.getName()
        );

        return new MenuItemResponse(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.isAvailableOnlyInRestaurant(),
                menuItem.getPhotoPath(),
                restaurantResponse
        );
    }
}
