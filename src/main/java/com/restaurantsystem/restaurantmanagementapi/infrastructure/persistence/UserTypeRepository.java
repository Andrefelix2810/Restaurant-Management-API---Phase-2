package com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTypeRepository extends JpaRepository<UserType, Long> {

    boolean existsByNameIgnoreCase(String name);
}
