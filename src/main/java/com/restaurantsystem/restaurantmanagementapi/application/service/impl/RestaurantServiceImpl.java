package com.restaurantsystem.restaurantmanagementapi.application.service.impl;

import com.restaurantsystem.restaurantmanagementapi.application.service.RestaurantService;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.ResourceNotFoundException;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.RestaurantRepository;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.UserRepository;
import com.restaurantsystem.restaurantmanagementapi.mapper.RestaurantMapper;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.RestaurantCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.RestaurantUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.RestaurantResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private static final String RESTAURANT_OWNER_TYPE = "DONO_RESTAURANTE";

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final RestaurantMapper restaurantMapper;

    public RestaurantServiceImpl(
            RestaurantRepository restaurantRepository,
            UserRepository userRepository,
            RestaurantMapper restaurantMapper
    ) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.restaurantMapper = restaurantMapper;
    }

    @Override
    public RestaurantResponse create(RestaurantCreateRequest request) {
        User owner = findOwner(request.getOwnerId());
        validateOwnerType(owner);

        Restaurant restaurant = restaurantMapper.toEntity(request, owner);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(savedRestaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponse> findAll() {
        return restaurantRepository.findAll()
                .stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantResponse findById(Long id) {
        Restaurant restaurant = findRestaurant(id);
        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    public RestaurantResponse update(Long id, RestaurantUpdateRequest request) {
        Restaurant restaurant = findRestaurant(id);
        User owner = findOwner(request.getOwnerId());
        validateOwnerType(owner);

        restaurantMapper.updateEntity(restaurant, request, owner);
        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponse(updatedRestaurant);
    }

    @Override
    public void delete(Long id) {
        Restaurant restaurant = findRestaurant(id);
        restaurantRepository.delete(restaurant);
    }

    private Restaurant findRestaurant(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", id));
    }

    private User findOwner(Long ownerId) {
        return userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", ownerId));
    }

    private void validateOwnerType(User owner) {
        if (owner.getUserType() == null
                || owner.getUserType().getName() == null
                || !RESTAURANT_OWNER_TYPE.equalsIgnoreCase(owner.getUserType().getName().trim())) {
            throw new BusinessException("Restaurant owner must have user type DONO_RESTAURANTE");
        }
    }
}
