package com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.adapter;

import com.restaurantsystem.restaurantmanagementapi.application.port.out.MenuItemPersistencePort;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.MenuItem;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.entity.JpaRestaurantEntity;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.jpa.JpaMenuItemRepository;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.jpa.JpaRestaurantRepository;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.mapper.MenuItemPersistenceMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class MenuItemPersistenceAdapter implements MenuItemPersistencePort {

    private final JpaMenuItemRepository repository;
    private final JpaRestaurantRepository restaurantRepository;

    public MenuItemPersistenceAdapter(
            JpaMenuItemRepository repository,
            JpaRestaurantRepository restaurantRepository
    ) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @Transactional
    public MenuItem save(MenuItem menuItem) {
        JpaRestaurantEntity restaurant = restaurantRepository.getReferenceById(menuItem.getRestaurant().getId());
        return MenuItemPersistenceMapper.toDomain(
                repository.save(MenuItemPersistenceMapper.toEntity(menuItem, restaurant))
        );
    }

    @Override
    public List<MenuItem> findAll() {
        return repository.findAll().stream()
                .map(MenuItemPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<MenuItem> findById(Long id) {
        return repository.findById(id).map(MenuItemPersistenceMapper::toDomain);
    }

    @Override
    @Transactional
    public void delete(MenuItem menuItem) {
        repository.deleteById(menuItem.getId());
    }

    @Override
    public List<MenuItem> findAllByRestaurantId(Long restaurantId) {
        return repository.findAllByRestaurantId(restaurantId).stream()
                .map(MenuItemPersistenceMapper::toDomain)
                .toList();
    }
}
