package com.restaurantsystem.restaurantmanagementapi.controller;

import com.restaurantsystem.restaurantmanagementapi.dto.request.MenuItemCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.request.MenuItemUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.response.MenuItemResponse;
import com.restaurantsystem.restaurantmanagementapi.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurants/{restaurantId}/menu-items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @PostMapping
    public ResponseEntity<MenuItemResponse> create(
            @PathVariable Long restaurantId,
            @Valid @RequestBody MenuItemCreateRequest request
    ) {
        MenuItemResponse response = menuItemService.create(restaurantId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> findByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuItemService.findByRestaurantId(restaurantId));
    }

    @PutMapping("/{menuItemId}")
    public ResponseEntity<MenuItemResponse> update(
            @PathVariable Long restaurantId,
            @PathVariable Long menuItemId,
            @Valid @RequestBody MenuItemUpdateRequest request
    ) {
        return ResponseEntity.ok(menuItemService.update(restaurantId, menuItemId, request));
    }

    @DeleteMapping("/{menuItemId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long restaurantId,
            @PathVariable Long menuItemId
    ) {
        menuItemService.delete(restaurantId, menuItemId);
        return ResponseEntity.noContent().build();
    }
}
