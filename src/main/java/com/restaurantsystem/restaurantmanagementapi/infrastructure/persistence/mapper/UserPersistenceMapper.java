package com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.mapper;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.Address;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.entity.JpaAddress;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.entity.JpaUserEntity;
import com.restaurantsystem.restaurantmanagementapi.infrastructure.persistence.entity.JpaUserTypeEntity;

public final class UserPersistenceMapper {

    private UserPersistenceMapper() {
    }

    public static User toDomain(JpaUserEntity entity) {
        return User.restore(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getLogin(),
                entity.getPassword(),
                entity.getLastModifiedDate(),
                UserTypePersistenceMapper.toDomain(entity.getUserType()),
                toDomain(entity.getAddress())
        );
    }

    public static JpaUserEntity toEntity(User user, JpaUserTypeEntity userType) {
        return new JpaUserEntity(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getPassword(),
                user.getLastModifiedDate(),
                userType,
                toEntity(user.getAddress())
        );
    }

    private static Address toDomain(JpaAddress address) {
        if (address == null) {
            return null;
        }
        return new Address(
                address.getStreet(),
                address.getNumber(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getZipCode(),
                address.getComplement()
        );
    }

    private static JpaAddress toEntity(Address address) {
        if (address == null) {
            return null;
        }
        return new JpaAddress(
                address.getStreet(),
                address.getNumber(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getZipCode(),
                address.getComplement()
        );
    }
}
