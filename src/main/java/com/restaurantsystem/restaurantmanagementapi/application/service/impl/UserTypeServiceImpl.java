package com.restaurantsystem.restaurantmanagementapi.application.service.impl;

import com.restaurantsystem.restaurantmanagementapi.application.port.in.UserTypeUseCase;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.UserTypeCommand;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.UserPersistencePort;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.UserTypePersistencePort;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.ResourceNotFoundException;

import java.util.List;

public class UserTypeServiceImpl implements UserTypeUseCase {

    private final UserTypePersistencePort userTypeGateway;
    private final UserPersistencePort userGateway;

    public UserTypeServiceImpl(UserTypePersistencePort userTypeGateway, UserPersistencePort userGateway) {
        this.userTypeGateway = userTypeGateway;
        this.userGateway = userGateway;
    }

    @Override
    public UserType create(UserTypeCommand command) {
        UserType userType = UserType.create(command.name());
        if (userTypeGateway.existsByNameIgnoreCase(userType.getName())) {
            throw new BusinessException(
                    "User type already registered. Reuse its id when creating users"
            );
        }
        return userTypeGateway.save(userType);
    }

    @Override
    public List<UserType> findAll() {
        return userTypeGateway.findAll();
    }

    @Override
    public UserType findById(Long id) {
        return findUserType(id);
    }

    @Override
    public UserType update(Long id, UserTypeCommand command) {
        UserType userType = findUserType(id);
        String previousName = userType.getName();
        userType.rename(command.name());
        boolean nameChanged = !previousName.equalsIgnoreCase(userType.getName());

        if (nameChanged && userTypeGateway.existsByNameIgnoreCase(userType.getName())) {
            throw new BusinessException("User type already registered");
        }
        if (nameChanged && userGateway.existsByUserTypeId(id)) {
            throw new BusinessException(
                    "User type is associated with users and cannot be changed. Update the userTypeId of the user instead"
            );
        }
        return userTypeGateway.save(userType);
    }

    @Override
    public void delete(Long id) {
        UserType userType = findUserType(id);
        if (userGateway.existsByUserTypeId(id)) {
            throw new BusinessException("User type is associated with users and cannot be deleted");
        }
        userTypeGateway.delete(userType);
    }

    private UserType findUserType(Long id) {
        return userTypeGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserType", id));
    }
}
