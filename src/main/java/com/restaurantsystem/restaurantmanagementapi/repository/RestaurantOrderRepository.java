package com.restaurantsystem.restaurantmanagementapi.repository;

import com.restaurantsystem.restaurantmanagementapi.entity.RestaurantOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantOrderRepository extends JpaRepository<RestaurantOrder, Long> {

    Optional<RestaurantOrder> findByIdAndRestaurantId(Long orderId, Long restaurantId);

    List<RestaurantOrder> findByRestaurantIdAndTableId(Long restaurantId, Long tableId);
}
