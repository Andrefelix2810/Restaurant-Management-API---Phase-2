package com.restaurantsystem.restaurantmanagementapi.domain.entity;

import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;
import com.restaurantsystem.restaurantmanagementapi.domain.enums.UserTypeName;

import java.time.LocalDateTime;

public class UserType {

    private final Long id;
    private String name;
    private final String description;
    private final boolean active;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UserType(
            Long id,
            String name,
            String description,
            boolean active,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = validateAndNormalizeName(name);
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserType create(String name) {
        LocalDateTime now = LocalDateTime.now();
        return new UserType(null, name, null, true, now, now);
    }

    public static UserType restore(
            Long id,
            String name,
            String description,
            boolean active,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new UserType(id, name, description, active, createdAt, updatedAt);
    }

    public void rename(String name) {
        this.name = validateAndNormalizeName(name);
        this.updatedAt = LocalDateTime.now();
    }

    private static String validateAndNormalizeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BusinessException("Name is required");
        }

        return UserTypeName.from(name).name();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
