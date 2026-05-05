package com.restaurantsystem.restaurantmanagementapi.service.impl;

import com.restaurantsystem.restaurantmanagementapi.dto.request.OrderCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.response.OrderResponse;
import com.restaurantsystem.restaurantmanagementapi.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.entity.RestaurantOrder;
import com.restaurantsystem.restaurantmanagementapi.entity.RestaurantTable;
import com.restaurantsystem.restaurantmanagementapi.enums.OrderStatus;
import com.restaurantsystem.restaurantmanagementapi.exception.BusinessException;
import com.restaurantsystem.restaurantmanagementapi.exception.ResourceNotFoundException;
import com.restaurantsystem.restaurantmanagementapi.repository.RestaurantOrderRepository;
import com.restaurantsystem.restaurantmanagementapi.repository.RestaurantRepository;
import com.restaurantsystem.restaurantmanagementapi.repository.RestaurantTableRepository;
import com.restaurantsystem.restaurantmanagementapi.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final RestaurantOrderRepository restaurantOrderRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    public OrderServiceImpl(
            RestaurantOrderRepository restaurantOrderRepository,
            RestaurantRepository restaurantRepository,
            RestaurantTableRepository restaurantTableRepository
    ) {
        this.restaurantOrderRepository = restaurantOrderRepository;
        this.restaurantRepository = restaurantRepository;
        this.restaurantTableRepository = restaurantTableRepository;
    }

    @Override
    public OrderResponse openOrder(Long restaurantId, OrderCreateRequest request) {
        Restaurant restaurant = findRestaurantById(restaurantId);

        RestaurantTable table = restaurantTableRepository.findById(request.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table with id " + request.getTableId() + " not found"));

        if (!table.getRestaurant().getId().equals(restaurantId)) {
            throw new BusinessException("Table does not belong to restaurant with id " + restaurantId);
        }

        LocalDateTime now = LocalDateTime.now();
        RestaurantOrder order = new RestaurantOrder();
        order.setRestaurant(restaurant);
        order.setTable(table);
        order.setStatus(OrderStatus.OPEN);
        order.setNotes(request.getNotes());
        order.setCreatedAt(now);
        order.setUpdatedAt(now);

        RestaurantOrder savedOrder = restaurantOrderRepository.save(order);
        return toResponse(savedOrder);
    }

    @Override
    public OrderResponse findById(Long restaurantId, Long orderId) {
        RestaurantOrder order = restaurantOrderRepository.findByIdAndRestaurantId(orderId, restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order with id " + orderId + " not found for restaurant with id " + restaurantId));

        return toResponse(order);
    }

    @Override
    public List<OrderResponse> findByTable(Long restaurantId, Long tableId) {
        findRestaurantById(restaurantId);

        RestaurantTable table = restaurantTableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table with id " + tableId + " not found"));

        if (!table.getRestaurant().getId().equals(restaurantId)) {
            throw new BusinessException("Table does not belong to restaurant with id " + restaurantId);
        }

        return restaurantOrderRepository.findByRestaurantIdAndTableId(restaurantId, tableId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Restaurant findRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant with id " + id + " not found"));
    }

    private OrderResponse toResponse(RestaurantOrder order) {
        return new OrderResponse(
                order.getId(),
                order.getRestaurant().getId(),
                order.getTable().getId(),
                order.getStatus(),
                order.getNotes(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
