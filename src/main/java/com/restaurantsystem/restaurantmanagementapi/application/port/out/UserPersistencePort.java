package com.restaurantsystem.restaurantmanagementapi.application.port.out;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserPersistencePort {

    User save(User user);

    List<User> findAll();

    Optional<User> findById(Long id);

    void delete(User user);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

    boolean existsByUserTypeId(Long userTypeId);

    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);
}
