package com.restaurantsystem.restaurantmanagementapi.presentation.controller;

import com.restaurantsystem.restaurantmanagementapi.application.service.MenuItemService;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.MenuItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurants/{restaurantId}/menu-items")
public class RestaurantMenuItemController {

    private final MenuItemService menuItemService;

    public RestaurantMenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> findByRestaurantId(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuItemService.findByRestaurantId(restaurantId));
    }
}
