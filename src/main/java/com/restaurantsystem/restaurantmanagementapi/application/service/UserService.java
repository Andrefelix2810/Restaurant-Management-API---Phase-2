package com.restaurantsystem.restaurantmanagementapi.application.service;

import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse create(UserCreateRequest request);

    List<UserResponse> findAll();

    UserResponse findById(Long id);

    UserResponse update(Long id, UserUpdateRequest request);

    void delete(Long id);
}
