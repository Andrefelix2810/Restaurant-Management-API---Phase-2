package com.restaurantsystem.restaurantmanagementapi.application.service;

import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.RestaurantCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.RestaurantUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.RestaurantResponse;

import java.util.List;

public interface RestaurantService {

    RestaurantResponse create(RestaurantCreateRequest request);

    List<RestaurantResponse> findAll();

    RestaurantResponse findById(Long id);

    RestaurantResponse update(Long id, RestaurantUpdateRequest request);

    void delete(Long id);
}
