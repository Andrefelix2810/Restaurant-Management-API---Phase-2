package com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.jpa;

import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.entity.JpaUserTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserTypeRepository extends JpaRepository<JpaUserTypeEntity, Long> {

    boolean existsByNameIgnoreCase(String name);
}
