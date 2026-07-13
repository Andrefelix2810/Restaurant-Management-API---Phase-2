package com.restaurantsystem.restaurantmanagementapi.application.service.impl;

import com.restaurantsystem.restaurantmanagementapi.application.service.MenuItemService;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.MenuItem;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.ResourceNotFoundException;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.MenuItemRepository;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.RestaurantRepository;
import com.restaurantsystem.restaurantmanagementapi.mapper.MenuItemMapper;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.MenuItemCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.MenuItemUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.MenuItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemMapper menuItemMapper;

    public MenuItemServiceImpl(
            MenuItemRepository menuItemRepository,
            RestaurantRepository restaurantRepository,
            MenuItemMapper menuItemMapper
    ) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemMapper = menuItemMapper;
    }

    @Override
    public MenuItemResponse create(MenuItemCreateRequest request) {
        Restaurant restaurant = findRestaurant(request.getRestaurantId());
        MenuItem menuItem = menuItemMapper.toEntity(request, restaurant);
        return menuItemMapper.toResponse(menuItemRepository.save(menuItem));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> findAll() {
        return menuItemRepository.findAll().stream()
                .map(menuItemMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemResponse findById(Long id) {
        return menuItemMapper.toResponse(findMenuItem(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemResponse> findByRestaurantId(Long restaurantId) {
        findRestaurant(restaurantId);
        return menuItemRepository.findAllByRestaurantId(restaurantId).stream()
                .map(menuItemMapper::toResponse)
                .toList();
    }

    @Override
    public MenuItemResponse update(Long id, MenuItemUpdateRequest request) {
        MenuItem menuItem = findMenuItem(id);
        Restaurant restaurant = findRestaurant(request.getRestaurantId());
        menuItemMapper.updateEntity(menuItem, request, restaurant);
        return menuItemMapper.toResponse(menuItemRepository.save(menuItem));
    }

    @Override
    public void delete(Long id) {
        menuItemRepository.delete(findMenuItem(id));
    }

    private MenuItem findMenuItem(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item", id));
    }

    private Restaurant findRestaurant(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));
    }
}
