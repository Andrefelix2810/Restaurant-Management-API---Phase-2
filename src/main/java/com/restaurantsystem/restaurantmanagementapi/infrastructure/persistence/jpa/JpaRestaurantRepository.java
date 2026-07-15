package com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.jpa;

import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.entity.JpaRestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRestaurantRepository extends JpaRepository<JpaRestaurantEntity, Long> {
}
