package com.restaurantsystem.restaurantmanagementapi.service;

import com.restaurantsystem.restaurantmanagementapi.dto.request.RestaurantCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.request.RestaurantUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.response.RestaurantResponse;

import java.util.List;

public interface RestaurantService {

    RestaurantResponse create(RestaurantCreateRequest request);

    List<RestaurantResponse> findAll();

    RestaurantResponse findById(Long id);

    RestaurantResponse update(Long id, RestaurantUpdateRequest request);

    void delete(Long id);
}
