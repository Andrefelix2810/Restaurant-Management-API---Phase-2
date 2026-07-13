package com.restaurantsystem.restaurantmanagementapi.application.service.impl;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.MenuItem;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.ResourceNotFoundException;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.MenuItemRepository;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.RestaurantRepository;
import com.restaurantsystem.restaurantmanagementapi.mapper.MenuItemMapper;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.MenuItemCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.MenuItemUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.MenuItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceImplTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    private MenuItemServiceImpl menuItemService;

    @BeforeEach
    void setUp() {
        menuItemService = new MenuItemServiceImpl(menuItemRepository, restaurantRepository, new MenuItemMapper());
    }

    @Test
    void shouldCreateMenuItemSuccessfully() {
        Restaurant restaurant = restaurant(1L);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(invocation -> {
            MenuItem menuItem = invocation.getArgument(0);
            menuItem.setId(10L);
            return menuItem;
        });

        MenuItemResponse response = menuItemService.create(createRequest());

        assertEquals(10L, response.getId());
        assertEquals("Feijoada", response.getName());
        assertEquals(new BigDecimal("39.90"), response.getPrice());
        assertEquals(1L, response.getRestaurant().getId());
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void shouldThrowExceptionWhenRestaurantDoesNotExistOnCreate() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.create(createRequest()));

        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }

    @Test
    void shouldListAllMenuItems() {
        when(menuItemRepository.findAll()).thenReturn(List.of(menuItem(1L, restaurant(1L))));

        List<MenuItemResponse> response = menuItemService.findAll();

        assertEquals(1, response.size());
        assertEquals("Feijoada", response.get(0).getName());
    }

    @Test
    void shouldFindMenuItemById() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem(1L, restaurant(1L))));

        MenuItemResponse response = menuItemService.findById(1L);

        assertEquals(1L, response.getId());
    }

    @Test
    void shouldThrowExceptionWhenMenuItemDoesNotExist() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.findById(1L));
    }

    @Test
    void shouldListMenuItemsByRestaurant() {
        Restaurant restaurant = restaurant(1L);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.findAllByRestaurantId(1L)).thenReturn(List.of(menuItem(1L, restaurant)));

        List<MenuItemResponse> response = menuItemService.findByRestaurantId(1L);

        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getRestaurant().getId());
        verify(menuItemRepository).findAllByRestaurantId(1L);
    }

    @Test
    void shouldThrowExceptionWhenRestaurantDoesNotExistOnSearch() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.findByRestaurantId(99L));

        verify(menuItemRepository, never()).findAllByRestaurantId(99L);
    }

    @Test
    void shouldUpdateMenuItemSuccessfully() {
        MenuItem menuItem = menuItem(1L, restaurant(1L));
        Restaurant newRestaurant = restaurant(2L);
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(restaurantRepository.findById(2L)).thenReturn(Optional.of(newRestaurant));
        when(menuItemRepository.save(menuItem)).thenReturn(menuItem);

        MenuItemResponse response = menuItemService.update(1L, updateRequest());

        assertEquals("Feijoada completa", response.getName());
        assertEquals(new BigDecimal("49.90"), response.getPrice());
        assertEquals(2L, response.getRestaurant().getId());
    }

    @Test
    void shouldThrowExceptionWhenMenuItemDoesNotExistOnUpdate() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.update(1L, updateRequest()));

        verify(restaurantRepository, never()).findById(any());
    }

    @Test
    void shouldDeleteMenuItemSuccessfully() {
        MenuItem menuItem = menuItem(1L, restaurant(1L));
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        menuItemService.delete(1L);

        verify(menuItemRepository).delete(menuItem);
    }

    @Test
    void shouldThrowExceptionWhenMenuItemDoesNotExistOnDelete() {
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemService.delete(1L));

        verify(menuItemRepository, never()).delete(any(MenuItem.class));
    }

    private MenuItemCreateRequest createRequest() {
        return new MenuItemCreateRequest(
                "Feijoada", "Feijoada completa", new BigDecimal("39.90"),
                true, "/images/feijoada.jpg", 1L
        );
    }

    private MenuItemUpdateRequest updateRequest() {
        return new MenuItemUpdateRequest(
                "Feijoada completa", "Serve duas pessoas", new BigDecimal("49.90"),
                false, "/images/feijoada-completa.jpg", 2L
        );
    }

    private MenuItem menuItem(Long id, Restaurant restaurant) {
        return new MenuItem(
                id, "Feijoada", "Feijoada completa", new BigDecimal("39.90"),
                true, "/images/feijoada.jpg", restaurant
        );
    }

    private Restaurant restaurant(Long id) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        restaurant.setName("Restaurante Sabor Brasil " + id);
        return restaurant;
    }
}
