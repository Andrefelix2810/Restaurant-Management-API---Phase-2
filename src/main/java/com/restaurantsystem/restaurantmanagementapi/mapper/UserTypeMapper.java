package com.restaurantsystem.restaurantmanagementapi.mapper;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserTypeCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserTypeUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.UserTypeResponse;
import org.springframework.stereotype.Component;

@Component
public class UserTypeMapper {

    public UserType toEntity(UserTypeCreateRequest request) {
        UserType userType = new UserType();
        userType.setName(normalizeName(request.getName()));
        userType.setActive(true);
        return userType;
    }

    public void updateEntity(UserType userType, UserTypeUpdateRequest request) {
        userType.setName(normalizeName(request.getName()));
    }

    public UserTypeResponse toResponse(UserType userType) {
        if (userType == null) {
            return null;
        }

        return new UserTypeResponse(
                userType.getId(),
                userType.getName()
        );
    }

    private String normalizeName(String name) {
        return name == null ? null : name.trim();
    }
}
