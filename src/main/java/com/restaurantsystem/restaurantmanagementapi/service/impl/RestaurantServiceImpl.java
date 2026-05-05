package com.restaurantsystem.restaurantmanagementapi.service.impl;

import com.restaurantsystem.restaurantmanagementapi.dto.request.RestaurantCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.request.RestaurantUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.response.RestaurantResponse;
import com.restaurantsystem.restaurantmanagementapi.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.exception.ResourceNotFoundException;
import com.restaurantsystem.restaurantmanagementapi.repository.RestaurantRepository;
import com.restaurantsystem.restaurantmanagementapi.service.RestaurantService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public RestaurantResponse create(RestaurantCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();

        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setPhone(request.getPhone());
        restaurant.setActive(true);
        restaurant.setCreatedAt(now);
        restaurant.setUpdatedAt(now);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return toResponse(savedRestaurant);
    }

    @Override
    public List<RestaurantResponse> findAll() {
        return restaurantRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public RestaurantResponse findById(Long id) {
        return toResponse(findRestaurantById(id));
    }

    @Override
    public RestaurantResponse update(Long id, RestaurantUpdateRequest request) {
        Restaurant restaurant = findRestaurantById(id);
        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setPhone(request.getPhone());

        if (request.getActive() != null) {
            restaurant.setActive(request.getActive());
        }

        restaurant.setUpdatedAt(LocalDateTime.now());
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        return toResponse(updatedRestaurant);
    }

    @Override
    public void delete(Long id) {
        Restaurant restaurant = findRestaurantById(id);
        restaurantRepository.delete(restaurant);
    }

    private Restaurant findRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant with id " + id + " not found"));
    }

    private RestaurantResponse toResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getPhone(),
                restaurant.getActive(),
                restaurant.getCreatedAt(),
                restaurant.getUpdatedAt()
        );
    }
}
