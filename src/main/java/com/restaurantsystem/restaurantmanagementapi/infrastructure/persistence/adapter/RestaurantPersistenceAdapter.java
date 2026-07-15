package com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.adapter;

import com.restaurantsystem.restaurantmanagementapi.application.port.out.RestaurantPersistencePort;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Restaurant;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.entity.JpaUserEntity;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.jpa.JpaRestaurantRepository;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.jpa.JpaUserRepository;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.mapper.RestaurantPersistenceMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class RestaurantPersistenceAdapter implements RestaurantPersistencePort {

    private final JpaRestaurantRepository repository;
    private final JpaUserRepository userRepository;

    public RestaurantPersistenceAdapter(JpaRestaurantRepository repository, JpaUserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Restaurant save(Restaurant restaurant) {
        JpaUserEntity owner = userRepository.getReferenceById(restaurant.getOwner().getId());
        return RestaurantPersistenceMapper.toDomain(
                repository.save(RestaurantPersistenceMapper.toEntity(restaurant, owner))
        );
    }

    @Override
    public List<Restaurant> findAll() {
        return repository.findAll().stream()
                .map(RestaurantPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return repository.findById(id).map(RestaurantPersistenceMapper::toDomain);
    }

    @Override
    @Transactional
    public void delete(Restaurant restaurant) {
        repository.deleteById(restaurant.getId());
    }
}
