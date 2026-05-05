package com.restaurantsystem.restaurantmanagementapi.controller;

import com.restaurantsystem.restaurantmanagementapi.dto.request.TableCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.request.TableStatusUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.response.TableResponse;
import com.restaurantsystem.restaurantmanagementapi.service.RestaurantTableService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurants/{restaurantId}/tables")
public class RestaurantTableController {

    private final RestaurantTableService restaurantTableService;

    public RestaurantTableController(RestaurantTableService restaurantTableService) {
        this.restaurantTableService = restaurantTableService;
    }

    @PostMapping
    public ResponseEntity<TableResponse> create(
            @PathVariable Long restaurantId,
            @Valid @RequestBody TableCreateRequest request
    ) {
        TableResponse response = restaurantTableService.create(restaurantId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TableResponse>> findByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(restaurantTableService.findByRestaurantId(restaurantId));
    }

    @PatchMapping("/{tableId}/status")
    public ResponseEntity<TableResponse> updateStatus(
            @PathVariable Long restaurantId,
            @PathVariable Long tableId,
            @Valid @RequestBody TableStatusUpdateRequest request
    ) {
        return ResponseEntity.ok(restaurantTableService.updateStatus(restaurantId, tableId, request));
    }
}
