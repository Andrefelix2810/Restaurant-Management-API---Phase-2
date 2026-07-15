package com.restaurantsystem.restaurantmanagementapi.presentation.presenter;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.UserTypeResponse;

public final class UserTypePresenter {

    private UserTypePresenter() {
    }

    public static UserTypeResponse toResponse(UserType userType) {
        if (userType == null) {
            return null;
        }
        return new UserTypeResponse(userType.getId(), userType.getName());
    }
}
