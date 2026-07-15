package com.restaurantsystem.restaurantmanagementapi.application.service.impl;

import com.restaurantsystem.restaurantmanagementapi.application.port.in.RestaurantUseCase;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.RestaurantCommand;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.RestaurantPersistencePort;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.UserPersistencePort;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;
import com.restaurantsystem.restaurantmanagementapi.domain.enums.UserTypeName;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.ResourceNotFoundException;

import java.util.List;

public class RestaurantServiceImpl implements RestaurantUseCase {

    private static final String RESTAURANT_OWNER_TYPE = UserTypeName.DONO_RESTAURANTE.name();

    private final RestaurantPersistencePort restaurantGateway;
    private final UserPersistencePort userGateway;

    public RestaurantServiceImpl(RestaurantPersistencePort restaurantGateway, UserPersistencePort userGateway) {
        this.restaurantGateway = restaurantGateway;
        this.userGateway = userGateway;
    }

    @Override
    public Restaurant create(RestaurantCommand command) {
        User owner = findOwner(command.ownerId());
        validateOwnerType(owner);
        Restaurant restaurant = Restaurant.create(
                command.name(),
                command.address(),
                command.cuisineType(),
                command.openingHours(),
                owner
        );
        return restaurantGateway.save(restaurant);
    }

    @Override
    public List<Restaurant> findAll() {
        return restaurantGateway.findAll();
    }

    @Override
    public Restaurant findById(Long id) {
        return findRestaurant(id);
    }

    @Override
    public Restaurant update(Long id, RestaurantCommand command) {
        Restaurant restaurant = findRestaurant(id);
        User owner = findOwner(command.ownerId());
        validateOwnerType(owner);
        restaurant.update(
                command.name(),
                command.address(),
                command.cuisineType(),
                command.openingHours(),
                owner
        );
        return restaurantGateway.save(restaurant);
    }

    @Override
    public void delete(Long id) {
        restaurantGateway.delete(findRestaurant(id));
    }

    private Restaurant findRestaurant(Long id) {
        return restaurantGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));
    }

    private User findOwner(Long id) {
        return userGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    private void validateOwnerType(User owner) {
        if (owner.getUserType() == null
                || owner.getUserType().getName() == null
                || !RESTAURANT_OWNER_TYPE.equalsIgnoreCase(owner.getUserType().getName().trim())) {
            throw new BusinessException("Restaurant owner must have user type DONO_RESTAURANTE");
        }
    }
}
