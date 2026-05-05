package com.restaurantsystem.restaurantmanagementapi.repository;

import com.restaurantsystem.restaurantmanagementapi.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    List<RestaurantTable> findByRestaurantId(Long restaurantId);

    boolean existsByRestaurantIdAndTableNumber(Long restaurantId, Integer tableNumber);
}
