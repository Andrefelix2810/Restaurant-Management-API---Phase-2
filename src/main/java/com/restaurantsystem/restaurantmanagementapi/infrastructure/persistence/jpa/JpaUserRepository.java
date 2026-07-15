package com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.jpa;

import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.entity.JpaUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<JpaUserEntity, Long> {

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

    boolean existsByUserTypeId(Long userTypeId);

    Optional<JpaUserEntity> findByEmail(String email);

    Optional<JpaUserEntity> findByLogin(String login);
}
