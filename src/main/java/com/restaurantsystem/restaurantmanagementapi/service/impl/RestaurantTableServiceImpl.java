package com.restaurantsystem.restaurantmanagementapi.service.impl;

import com.restaurantsystem.restaurantmanagementapi.dto.request.TableCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.request.TableStatusUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.response.TableResponse;
import com.restaurantsystem.restaurantmanagementapi.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.entity.RestaurantTable;
import com.restaurantsystem.restaurantmanagementapi.exception.BusinessException;
import com.restaurantsystem.restaurantmanagementapi.exception.ResourceNotFoundException;
import com.restaurantsystem.restaurantmanagementapi.repository.RestaurantRepository;
import com.restaurantsystem.restaurantmanagementapi.repository.RestaurantTableRepository;
import com.restaurantsystem.restaurantmanagementapi.service.RestaurantTableService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantTableServiceImpl implements RestaurantTableService {

    private final RestaurantTableRepository restaurantTableRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantTableServiceImpl(
            RestaurantTableRepository restaurantTableRepository,
            RestaurantRepository restaurantRepository
    ) {
        this.restaurantTableRepository = restaurantTableRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public TableResponse create(Long restaurantId, TableCreateRequest request) {
        Restaurant restaurant = findRestaurantById(restaurantId);

        if (restaurantTableRepository.existsByRestaurantIdAndTableNumber(restaurantId, request.getTableNumber())) {
            throw new BusinessException("Table number already exists for this restaurant");
        }

        RestaurantTable table = new RestaurantTable();
        table.setTableNumber(request.getTableNumber());
        table.setSeats(request.getSeats());
        table.setStatus(request.getStatus());
        table.setRestaurant(restaurant);

        RestaurantTable savedTable = restaurantTableRepository.save(table);
        return toResponse(savedTable);
    }

    @Override
    public List<TableResponse> findByRestaurantId(Long restaurantId) {
        findRestaurantById(restaurantId);

        return restaurantTableRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public TableResponse updateStatus(Long restaurantId, Long tableId, TableStatusUpdateRequest request) {
        findRestaurantById(restaurantId);

        RestaurantTable table = restaurantTableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Table with id " + tableId + " not found"));

        if (!table.getRestaurant().getId().equals(restaurantId)) {
            throw new BusinessException("Table does not belong to restaurant with id " + restaurantId);
        }

        table.setStatus(request.getStatus());
        RestaurantTable updatedTable = restaurantTableRepository.save(table);
        return toResponse(updatedTable);
    }

    private Restaurant findRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant with id " + id + " not found"));
    }

    private TableResponse toResponse(RestaurantTable table) {
        return new TableResponse(
                table.getId(),
                table.getRestaurant().getId(),
                table.getTableNumber(),
                table.getSeats(),
                table.getStatus()
        );
    }
}
