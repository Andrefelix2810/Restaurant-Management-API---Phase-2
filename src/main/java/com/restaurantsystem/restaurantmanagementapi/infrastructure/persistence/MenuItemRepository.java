package com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findAllByRestaurantId(Long restaurantId);
}
