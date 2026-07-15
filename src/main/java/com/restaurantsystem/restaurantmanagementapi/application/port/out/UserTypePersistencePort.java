package com.restaurantsystem.restaurantmanagementapi.application.port.out;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;

import java.util.List;
import java.util.Optional;

public interface UserTypePersistencePort {

    UserType save(UserType userType);

    List<UserType> findAll();

    Optional<UserType> findById(Long id);

    void delete(UserType userType);

    boolean existsByNameIgnoreCase(String name);
}
