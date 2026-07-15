package com.restaurantsystem.restaurantmanagementapi.application.service.impl;

import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.MenuItemCommand;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.MenuItemPersistencePort;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.RestaurantPersistencePort;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Address;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.MenuItem;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceImplTest {

    @Mock
    private MenuItemPersistencePort menuItemGateway;

    @Mock
    private RestaurantPersistencePort restaurantGateway;

    private MenuItemServiceImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new MenuItemServiceImpl(menuItemGateway, restaurantGateway);
    }

    @Test
    void shouldCreateMenuItem() {
        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant()));
        when(menuItemGateway.save(any(MenuItem.class))).thenAnswer(invocation -> withId(invocation.getArgument(0), 10L));

        MenuItem created = useCase.create(command());

        assertEquals(10L, created.getId());
        assertEquals("Feijoada", created.getName());
    }

    @Test
    void shouldRejectUnknownRestaurantOnCreate() {
        when(restaurantGateway.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.create(command()));
    }

    @Test
    void shouldFindAllMenuItems() {
        when(menuItemGateway.findAll()).thenReturn(List.of(menuItem(1L), menuItem(2L)));

        assertEquals(2, useCase.findAll().size());
    }

    @Test
    void shouldFindMenuItemById() {
        when(menuItemGateway.findById(1L)).thenReturn(Optional.of(menuItem(1L)));

        assertEquals(1L, useCase.findById(1L).getId());
    }

    @Test
    void shouldRejectUnknownMenuItem() {
        when(menuItemGateway.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.findById(99L));
    }

    @Test
    void shouldFindMenuItemsByRestaurant() {
        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant()));
        when(menuItemGateway.findAllByRestaurantId(1L)).thenReturn(List.of(menuItem(1L)));

        assertEquals(1, useCase.findByRestaurantId(1L).size());
    }

    @Test
    void shouldRejectUnknownRestaurantWhenListingMenuItems() {
        when(restaurantGateway.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.findByRestaurantId(99L));
    }

    @Test
    void shouldUpdateMenuItem() {
        MenuItem existing = menuItem(1L);
        when(menuItemGateway.findById(1L)).thenReturn(Optional.of(existing));
        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant()));
        when(menuItemGateway.save(existing)).thenReturn(existing);

        MenuItem updated = useCase.update(1L, command());

        assertEquals(new BigDecimal("39.90"), updated.getPrice());
    }

    @Test
    void shouldRejectUnknownRestaurantOnUpdate() {
        when(menuItemGateway.findById(1L)).thenReturn(Optional.of(menuItem(1L)));
        when(restaurantGateway.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.update(1L, command()));
    }

    @Test
    void shouldDeleteMenuItem() {
        MenuItem menuItem = menuItem(1L);
        when(menuItemGateway.findById(1L)).thenReturn(Optional.of(menuItem));

        useCase.delete(1L);

        verify(menuItemGateway).delete(menuItem);
    }

    private MenuItemCommand command() {
        return new MenuItemCommand(
                "Feijoada",
                "Feijoada completa",
                new BigDecimal("39.90"),
                true,
                "/images/feijoada.jpg",
                1L
        );
    }

    private MenuItem menuItem(Long id) {
        return MenuItem.restore(
                id,
                "Feijoada",
                "Feijoada completa",
                new BigDecimal("39.90"),
                true,
                "/images/feijoada.jpg",
                restaurant()
        );
    }

    private MenuItem withId(MenuItem menuItem, Long id) {
        return MenuItem.restore(
                id,
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.isAvailableOnlyInRestaurant(),
                menuItem.getPhotoPath(),
                menuItem.getRestaurant()
        );
    }

    private Restaurant restaurant() {
        LocalDateTime now = LocalDateTime.now();
        UserType type = UserType.restore(2L, "DONO_RESTAURANTE", null, true, now, now);
        Address address = new Address("Rua A", "10", "Centro", "Sao Paulo", "SP", "01000-000", null);
        User owner = User.restore(1L, "Joao", "joao@email.com", "joao", "123456", now, type, address);
        return Restaurant.restore(1L, "Sabor Brasil", "Rua A", "Brasileira", "11:00-23:00", owner);
    }
}
