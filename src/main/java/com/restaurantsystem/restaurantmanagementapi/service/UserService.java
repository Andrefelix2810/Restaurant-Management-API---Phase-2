package com.restaurantsystem.restaurantmanagementapi.service;

import com.restaurantsystem.restaurantmanagementapi.dto.request.UserCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.request.UserUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse create(UserCreateRequest request);

    List<UserResponse> findAll();

    UserResponse findById(Long id);

    UserResponse update(Long id, UserUpdateRequest request);

    void delete(Long id);
}