package com.restaurantsystem.restaurantmanagementapi.application.service.impl;

import com.restaurantsystem.restaurantmanagementapi.application.port.in.MenuItemUseCase;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.MenuItemCommand;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.MenuItemPersistencePort;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.RestaurantPersistencePort;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.MenuItem;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.ResourceNotFoundException;

import java.util.List;

public class MenuItemServiceImpl implements MenuItemUseCase {

    private final MenuItemPersistencePort menuItemGateway;
    private final RestaurantPersistencePort restaurantGateway;

    public MenuItemServiceImpl(
            MenuItemPersistencePort menuItemGateway,
            RestaurantPersistencePort restaurantGateway
    ) {
        this.menuItemGateway = menuItemGateway;
        this.restaurantGateway = restaurantGateway;
    }

    @Override
    public MenuItem create(MenuItemCommand command) {
        Restaurant restaurant = findRestaurant(command.restaurantId());
        MenuItem menuItem = MenuItem.create(
                command.name(),
                command.description(),
                command.price(),
                command.availableOnlyInRestaurant(),
                command.photoPath(),
                restaurant
        );
        return menuItemGateway.save(menuItem);
    }

    @Override
    public List<MenuItem> findAll() {
        return menuItemGateway.findAll();
    }

    @Override
    public MenuItem findById(Long id) {
        return findMenuItem(id);
    }

    @Override
    public List<MenuItem> findByRestaurantId(Long restaurantId) {
        findRestaurant(restaurantId);
        return menuItemGateway.findAllByRestaurantId(restaurantId);
    }

    @Override
    public MenuItem update(Long id, MenuItemCommand command) {
        MenuItem menuItem = findMenuItem(id);
        Restaurant restaurant = findRestaurant(command.restaurantId());
        menuItem.update(
                command.name(),
                command.description(),
                command.price(),
                command.availableOnlyInRestaurant(),
                command.photoPath(),
                restaurant
        );
        return menuItemGateway.save(menuItem);
    }

    @Override
    public void delete(Long id) {
        menuItemGateway.delete(findMenuItem(id));
    }

    private MenuItem findMenuItem(Long id) {
        return menuItemGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item", id));
    }

    private Restaurant findRestaurant(Long id) {
        return restaurantGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));
    }
}
