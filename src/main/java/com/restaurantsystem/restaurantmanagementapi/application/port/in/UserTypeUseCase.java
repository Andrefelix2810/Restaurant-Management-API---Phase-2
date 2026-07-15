package com.restaurantsystem.restaurantmanagementapi.application.port.in;

import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.UserTypeCommand;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;

import java.util.List;

public interface UserTypeUseCase {

    UserType create(UserTypeCommand command);

    List<UserType> findAll();

    UserType findById(Long id);

    UserType update(Long id, UserTypeCommand command);

    void delete(Long id);
}
