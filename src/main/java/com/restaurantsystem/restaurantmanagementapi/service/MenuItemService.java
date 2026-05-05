package com.restaurantsystem.restaurantmanagementapi.service;

import com.restaurantsystem.restaurantmanagementapi.dto.request.MenuItemCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.request.MenuItemUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.response.MenuItemResponse;

import java.util.List;

public interface MenuItemService {

    MenuItemResponse create(Long restaurantId, MenuItemCreateRequest request);

    List<MenuItemResponse> findByRestaurantId(Long restaurantId);

    MenuItemResponse update(Long restaurantId, Long menuItemId, MenuItemUpdateRequest request);

    void delete(Long restaurantId, Long menuItemId);
}
