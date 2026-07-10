package com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

    Optional<User> findByEmail(String email);

    Optional<User> findByLogin(String login);
}
