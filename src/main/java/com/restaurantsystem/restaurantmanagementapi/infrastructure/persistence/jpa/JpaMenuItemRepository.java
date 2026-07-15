package com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.jpa;

import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.entity.JpaMenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaMenuItemRepository extends JpaRepository<JpaMenuItemEntity, Long> {

    List<JpaMenuItemEntity> findAllByRestaurantId(Long restaurantId);
}
