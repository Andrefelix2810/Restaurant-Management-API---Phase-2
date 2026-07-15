package com.restaurantsystem.restaurantmanagementapi.application.port.out;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantPersistencePort {

    Restaurant save(Restaurant restaurant);

    List<Restaurant> findAll();

    Optional<Restaurant> findById(Long id);

    void delete(Restaurant restaurant);
}
