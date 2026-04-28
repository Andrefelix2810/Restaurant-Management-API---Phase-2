package com.restaurantsystem.restaurantmanagementapi.service;

import com.restaurantsystem.restaurantmanagementapi.dto.request.LoginRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.request.PasswordUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.request.UserCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.request.UserUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.response.UserResponse;
import com.restaurantsystem.restaurantmanagementapi.enums.Role;

import java.util.List;

public interface UserService {

    UserResponse create(UserCreateRequest request, Role role);

    List<UserResponse> findAll();

    UserResponse findById(Long id);

    UserResponse update(Long id, UserUpdateRequest request);

    void updatePassword(Long id, PasswordUpdateRequest request);

    UserResponse login(LoginRequest request);

    void delete(Long id);
}