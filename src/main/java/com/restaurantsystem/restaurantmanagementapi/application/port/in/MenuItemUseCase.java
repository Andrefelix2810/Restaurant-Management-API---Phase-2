package com.restaurantsystem.restaurantmanagementapi.application.port.in;

import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.MenuItemCommand;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.MenuItem;

import java.util.List;

public interface MenuItemUseCase {

    MenuItem create(MenuItemCommand command);

    List<MenuItem> findAll();

    MenuItem findById(Long id);

    List<MenuItem> findByRestaurantId(Long restaurantId);

    MenuItem update(Long id, MenuItemCommand command);

    void delete(Long id);
}
