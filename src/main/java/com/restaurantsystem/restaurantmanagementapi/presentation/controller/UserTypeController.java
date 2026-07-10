package com.restaurantsystem.restaurantmanagementapi.presentation.controller;

import com.restaurantsystem.restaurantmanagementapi.application.service.UserTypeService;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserTypeCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserTypeUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.UserTypeResponse;
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
@RequestMapping("/user-types")
public class UserTypeController {

    private final UserTypeService userTypeService;

    public UserTypeController(UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }

    @PostMapping
    public ResponseEntity<UserTypeResponse> create(@Valid @RequestBody UserTypeCreateRequest request) {
        UserTypeResponse response = userTypeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserTypeResponse>> findAll() {
        return ResponseEntity.ok(userTypeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserTypeResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userTypeService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserTypeResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UserTypeUpdateRequest request
    ) {
        return ResponseEntity.ok(userTypeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
