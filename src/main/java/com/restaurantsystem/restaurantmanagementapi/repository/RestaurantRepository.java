package com.restaurantsystem.restaurantmanagementapi.repository;

import com.restaurantsystem.restaurantmanagementapi.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
