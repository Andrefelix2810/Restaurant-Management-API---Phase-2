package com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.mapper;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.entity.JpaUserTypeEntity;

public final class UserTypePersistenceMapper {

    private UserTypePersistenceMapper() {
    }

    public static UserType toDomain(JpaUserTypeEntity entity) {
        return UserType.restore(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                Boolean.TRUE.equals(entity.getActive()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static JpaUserTypeEntity toEntity(UserType userType) {
        return new JpaUserTypeEntity(
                userType.getId(),
                userType.getName(),
                userType.getDescription(),
                userType.getActive(),
                userType.getCreatedAt(),
                userType.getUpdatedAt()
        );
    }
}
