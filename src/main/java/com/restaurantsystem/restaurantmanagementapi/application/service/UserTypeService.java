package com.restaurantsystem.restaurantmanagementapi.application.service;

import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserTypeCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserTypeUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.UserTypeResponse;

import java.util.List;

public interface UserTypeService {

    UserTypeResponse create(UserTypeCreateRequest request);

    List<UserTypeResponse> findAll();

    UserTypeResponse findById(Long id);

    UserTypeResponse update(Long id, UserTypeUpdateRequest request);

    void delete(Long id);
}
