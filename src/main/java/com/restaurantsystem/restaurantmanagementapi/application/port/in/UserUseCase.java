package com.restaurantsystem.restaurantmanagementapi.application.port.in;

import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.UserCommand;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;

import java.util.List;

public interface UserUseCase {

    User create(UserCommand command);

    List<User> findAll();

    User findById(Long id);

    User update(Long id, UserCommand command);

    void delete(Long id);
}
