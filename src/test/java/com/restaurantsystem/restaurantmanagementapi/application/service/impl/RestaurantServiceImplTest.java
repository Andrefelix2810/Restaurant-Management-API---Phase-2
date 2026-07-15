package com.restaurantsystem.restaurantmanagementapi.application.service.impl;

import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.RestaurantCommand;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.RestaurantPersistencePort;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.UserPersistencePort;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Address;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @Mock
    private RestaurantPersistencePort restaurantGateway;

    @Mock
    private UserPersistencePort userGateway;

    private RestaurantServiceImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new RestaurantServiceImpl(restaurantGateway, userGateway);
    }

    @Test
    void shouldCreateRestaurantForOwner() {
        when(userGateway.findById(1L)).thenReturn(Optional.of(owner(1L, "DONO_RESTAURANTE")));
        when(restaurantGateway.save(any(Restaurant.class)))
                .thenAnswer(invocation -> withId(invocation.getArgument(0), 10L));

        Restaurant created = useCase.create(command());

        assertEquals(10L, created.getId());
        assertEquals("Sabor Brasil", created.getName());
    }

    @Test
    void shouldRejectUnknownOwner() {
        when(userGateway.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.create(command()));
    }

    @Test
    void shouldRejectOwnerWithCustomerType() {
        when(userGateway.findById(1L)).thenReturn(Optional.of(owner(1L, "CLIENTE")));

        assertThrows(BusinessException.class, () -> useCase.create(command()));

        verify(restaurantGateway, never()).save(any());
    }

    @Test
    void shouldFindAllRestaurants() {
        when(restaurantGateway.findAll()).thenReturn(List.of(restaurant(1L), restaurant(2L)));

        assertEquals(2, useCase.findAll().size());
    }

    @Test
    void shouldFindRestaurantById() {
        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant(1L)));

        assertEquals(1L, useCase.findById(1L).getId());
    }

    @Test
    void shouldRejectUnknownRestaurant() {
        when(restaurantGateway.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.findById(99L));
    }

    @Test
    void shouldUpdateRestaurant() {
        Restaurant existing = restaurant(1L);
        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(existing));
        when(userGateway.findById(1L)).thenReturn(Optional.of(owner(1L, "DONO_RESTAURANTE")));
        when(restaurantGateway.save(existing)).thenReturn(existing);

        Restaurant updated = useCase.update(1L, command());

        assertEquals("Sabor Brasil", updated.getName());
    }

    @Test
    void shouldRejectCustomerOwnerOnUpdate() {
        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant(1L)));
        when(userGateway.findById(1L)).thenReturn(Optional.of(owner(1L, "CLIENTE")));

        assertThrows(BusinessException.class, () -> useCase.update(1L, command()));
    }

    @Test
    void shouldDeleteRestaurant() {
        Restaurant restaurant = restaurant(1L);
        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant));

        useCase.delete(1L);

        verify(restaurantGateway).delete(restaurant);
    }

    private RestaurantCommand command() {
        return new RestaurantCommand(
                "Sabor Brasil",
                "Rua das Flores, 123",
                "Brasileira",
                "11:00-23:00",
                1L
        );
    }

    private Restaurant restaurant(Long id) {
        return Restaurant.restore(
                id,
                "Sabor Brasil",
                "Rua das Flores, 123",
                "Brasileira",
                "11:00-23:00",
                owner(1L, "DONO_RESTAURANTE")
        );
    }

    private Restaurant withId(Restaurant restaurant, Long id) {
        return Restaurant.restore(
                id,
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getCuisineType(),
                restaurant.getOpeningHours(),
                restaurant.getOwner()
        );
    }

    private User owner(Long id, String userTypeName) {
        LocalDateTime now = LocalDateTime.now();
        UserType userType = UserType.restore(2L, userTypeName, null, true, now, now);
        Address address = new Address("Rua A", "10", "Centro", "Sao Paulo", "SP", "01000-000", null);
        return User.restore(
                id,
                "Joao Silva",
                "joao@email.com",
                "joao",
                "123456",
                now,
                userType,
                address
        );
    }
}
