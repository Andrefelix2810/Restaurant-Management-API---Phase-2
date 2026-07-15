package com.restaurantsystem.restaurantmanagementapi.application.port.in;

import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.RestaurantCommand;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;

import java.util.List;

public interface RestaurantUseCase {

    Restaurant create(RestaurantCommand command);

    List<Restaurant> findAll();

    Restaurant findById(Long id);

    Restaurant update(Long id, RestaurantCommand command);

    void delete(Long id);
}
