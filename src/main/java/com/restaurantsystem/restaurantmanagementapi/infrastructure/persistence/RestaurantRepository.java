package com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
