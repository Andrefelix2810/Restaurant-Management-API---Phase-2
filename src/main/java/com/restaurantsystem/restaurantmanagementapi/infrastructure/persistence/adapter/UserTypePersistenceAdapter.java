package com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.adapter;

import com.restaurantsystem.restaurantmanagementapi.application.port.out.UserTypePersistencePort;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.jpa.JpaUserTypeRepository;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.mapper.UserTypePersistenceMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class UserTypePersistenceAdapter implements UserTypePersistencePort {

    private final JpaUserTypeRepository repository;

    public UserTypePersistenceAdapter(JpaUserTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public UserType save(UserType userType) {
        return UserTypePersistenceMapper.toDomain(
                repository.save(UserTypePersistenceMapper.toEntity(userType))
        );
    }

    @Override
    public List<UserType> findAll() {
        return repository.findAll().stream()
                .map(UserTypePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<UserType> findById(Long id) {
        return repository.findById(id).map(UserTypePersistenceMapper::toDomain);
    }

    @Override
    @Transactional
    public void delete(UserType userType) {
        repository.deleteById(userType.getId());
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return repository.existsByNameIgnoreCase(name);
    }
}
