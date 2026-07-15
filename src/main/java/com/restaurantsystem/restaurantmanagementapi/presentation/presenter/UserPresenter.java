package com.restaurantsystem.restaurantmanagementapi.presentation.presenter;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.UserResponse;

public final class UserPresenter {

    private UserPresenter() {
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getLastModifiedDate(),
                UserTypePresenter.toResponse(user.getUserType()),
                AddressPresenter.toResponse(user.getAddress())
        );
    }
}
