package com.restaurantsystem.restaurantmanagementapi.controller;

import com.restaurantsystem.restaurantmanagementapi.dto.request.RestaurantCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.request.RestaurantUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.response.RestaurantResponse;
import com.restaurantsystem.restaurantmanagementapi.service.RestaurantService;
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
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<RestaurantResponse> create(@Valid @RequestBody RestaurantCreateRequest request) {
        RestaurantResponse response = restaurantService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> findAll() {
        return ResponseEntity.ok(restaurantService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantUpdateRequest request
    ) {
        return ResponseEntity.ok(restaurantService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
