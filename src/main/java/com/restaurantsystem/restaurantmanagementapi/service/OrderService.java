package com.restaurantsystem.restaurantmanagementapi.service;

import com.restaurantsystem.restaurantmanagementapi.dto.request.OrderCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse openOrder(Long restaurantId, OrderCreateRequest request);

    OrderResponse findById(Long restaurantId, Long orderId);

    List<OrderResponse> findByTable(Long restaurantId, Long tableId);
}
