package com.restaurantsystem.restaurantmanagementapi.service;

import com.restaurantsystem.restaurantmanagementapi.dto.request.TableCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.request.TableStatusUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.response.TableResponse;

import java.util.List;

public interface RestaurantTableService {

    TableResponse create(Long restaurantId, TableCreateRequest request);

    List<TableResponse> findByRestaurantId(Long restaurantId);

    TableResponse updateStatus(Long restaurantId, Long tableId, TableStatusUpdateRequest request);
}
