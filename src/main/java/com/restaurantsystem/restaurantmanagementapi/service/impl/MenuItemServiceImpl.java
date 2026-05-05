package com.restaurantsystem.restaurantmanagementapi.service.impl;

import com.restaurantsystem.restaurantmanagementapi.dto.request.MenuItemCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.request.MenuItemUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.response.MenuItemResponse;
import com.restaurantsystem.restaurantmanagementapi.entity.MenuItem;
import com.restaurantsystem.restaurantmanagementapi.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.exception.BusinessException;
import com.restaurantsystem.restaurantmanagementapi.exception.ResourceNotFoundException;
import com.restaurantsystem.restaurantmanagementapi.repository.MenuItemRepository;
import com.restaurantsystem.restaurantmanagementapi.repository.RestaurantRepository;
import com.restaurantsystem.restaurantmanagementapi.service.MenuItemService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public MenuItemResponse create(Long restaurantId, MenuItemCreateRequest request) {
        validatePrice(request.getPrice());
        Restaurant restaurant = findRestaurantById(restaurantId);

        MenuItem menuItem = new MenuItem();
        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setAvailable(request.getAvailable());
        menuItem.setRestaurant(restaurant);

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return toResponse(savedMenuItem);
    }

    @Override
    public List<MenuItemResponse> findByRestaurantId(Long restaurantId) {
        findRestaurantById(restaurantId);

        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public MenuItemResponse update(Long restaurantId, Long menuItemId, MenuItemUpdateRequest request) {
        validatePrice(request.getPrice());
        findRestaurantById(restaurantId);

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item with id " + menuItemId + " not found"));

        if (!menuItem.getRestaurant().getId().equals(restaurantId)) {
            throw new BusinessException("Menu item does not belong to restaurant with id " + restaurantId);
        }

        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setAvailable(request.getAvailable());

        MenuItem updatedMenuItem = menuItemRepository.save(menuItem);
        return toResponse(updatedMenuItem);
    }

    @Override
    public void delete(Long restaurantId, Long menuItemId) {
        findRestaurantById(restaurantId);

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item with id " + menuItemId + " not found"));

        if (!menuItem.getRestaurant().getId().equals(restaurantId)) {
            throw new BusinessException("Menu item does not belong to restaurant with id " + restaurantId);
        }

        menuItemRepository.delete(menuItem);
    }

    private Restaurant findRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant with id " + id + " not found"));
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Menu item price must be greater than zero");
        }
    }

    private MenuItemResponse toResponse(MenuItem menuItem) {
        return new MenuItemResponse(
                menuItem.getId(),
                menuItem.getRestaurant().getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.getAvailable()
        );
    }
}
