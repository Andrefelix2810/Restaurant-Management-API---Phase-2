package com.restaurantsystem.restaurantmanagementapi.application.service;

import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.MenuItemCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.MenuItemUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.MenuItemResponse;

import java.util.List;

public interface MenuItemService {

    MenuItemResponse create(MenuItemCreateRequest request);

    List<MenuItemResponse> findAll();

    MenuItemResponse findById(Long id);

    List<MenuItemResponse> findByRestaurantId(Long restaurantId);

    MenuItemResponse update(Long id, MenuItemUpdateRequest request);

    void delete(Long id);
}
