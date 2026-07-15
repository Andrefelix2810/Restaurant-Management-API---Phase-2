package com.restaurantsystem.restaurantmanagementapi.application.port.out;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemPersistencePort {

    MenuItem save(MenuItem menuItem);

    List<MenuItem> findAll();

    Optional<MenuItem> findById(Long id);

    void delete(MenuItem menuItem);

    List<MenuItem> findAllByRestaurantId(Long restaurantId);
}
