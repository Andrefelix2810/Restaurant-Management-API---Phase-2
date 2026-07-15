package com.restaurantsystem.restaurantmanagementapi.application.service.impl;

import com.restaurantsystem.restaurantmanagementapi.application.port.in.UserUseCase;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.AddressCommand;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.UserCommand;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.UserPersistencePort;
import com.restaurantsystem.restaurantmanagementapi.application.port.out.UserTypePersistencePort;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.Address;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;
import com.restaurantsystem.restaurantmanagementapi.domain.exception.ResourceNotFoundException;

import java.util.List;

public class UserServiceImpl implements UserUseCase {

    private final UserPersistencePort userGateway;
    private final UserTypePersistencePort userTypeGateway;

    public UserServiceImpl(UserPersistencePort userGateway, UserTypePersistencePort userTypeGateway) {
        this.userGateway = userGateway;
        this.userTypeGateway = userTypeGateway;
    }

    @Override
    public User create(UserCommand command) {
        validateEmailAndLoginForCreate(command);
        UserType userType = findUserType(command.userTypeId());
        User user = User.create(
                command.name(),
                command.email(),
                command.login(),
                command.password(),
                userType,
                toAddress(command.address())
        );
        return userGateway.save(user);
    }

    @Override
    public List<User> findAll() {
        return userGateway.findAll();
    }

    @Override
    public User findById(Long id) {
        return findUser(id);
    }

    @Override
    public User update(Long id, UserCommand command) {
        User user = findUser(id);
        validateEmailAndLoginForUpdate(id, command);
        UserType userType = findUserType(command.userTypeId());
        user.update(
                command.name(),
                command.email(),
                command.login(),
                command.password(),
                userType,
                toAddress(command.address())
        );
        return userGateway.save(user);
    }

    @Override
    public void delete(Long id) {
        userGateway.delete(findUser(id));
    }

    private User findUser(Long id) {
        return userGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    private void validateEmailAndLoginForCreate(UserCommand command) {
        if (userGateway.existsByEmail(command.email())) {
            throw new BusinessException("Email already registered");
        }
        if (userGateway.existsByLogin(command.login())) {
            throw new BusinessException("Login already registered");
        }
    }

    private void validateEmailAndLoginForUpdate(Long id, UserCommand command) {
        userGateway.findByEmail(command.email())
                .filter(foundUser -> !foundUser.getId().equals(id))
                .ifPresent(foundUser -> {
                    throw new BusinessException("Email already registered");
                });

        userGateway.findByLogin(command.login())
                .filter(foundUser -> !foundUser.getId().equals(id))
                .ifPresent(foundUser -> {
                    throw new BusinessException("Login already registered");
                });
    }

    private UserType findUserType(Long id) {
        return userTypeGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserType", id));
    }

    private Address toAddress(AddressCommand address) {
        if (address == null) {
            return null;
        }
        return new Address(
                address.street(),
                address.number(),
                address.neighborhood(),
                address.city(),
                address.state(),
                address.zipCode(),
                address.complement()
        );
    }
}
