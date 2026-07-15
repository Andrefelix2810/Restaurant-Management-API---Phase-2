package com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.adapter;

import com.restaurantsystem.restaurantmanagementapi.application.port.out.UserPersistencePort;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.entity.JpaUserTypeEntity;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.jpa.JpaUserRepository;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.jpa.JpaUserTypeRepository;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.mapper.UserPersistenceMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class UserPersistenceAdapter implements UserPersistencePort {

    private final JpaUserRepository repository;
    private final JpaUserTypeRepository userTypeRepository;

    public UserPersistenceAdapter(JpaUserRepository repository, JpaUserTypeRepository userTypeRepository) {
        this.repository = repository;
        this.userTypeRepository = userTypeRepository;
    }

    @Override
    @Transactional
    public User save(User user) {
        JpaUserTypeEntity userType = userTypeRepository.getReferenceById(user.getUserType().getId());
        return UserPersistenceMapper.toDomain(
                repository.save(UserPersistenceMapper.toEntity(user, userType))
        );
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream()
                .map(UserPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id).map(UserPersistenceMapper::toDomain);
    }

    @Override
    @Transactional
    public void delete(User user) {
        repository.deleteById(user.getId());
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existsByLogin(String login) {
        return repository.existsByLogin(login);
    }

    @Override
    public boolean existsByUserTypeId(Long userTypeId) {
        return repository.existsByUserTypeId(userTypeId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email).map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login).map(UserPersistenceMapper::toDomain);
    }
}
