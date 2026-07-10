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
        userType.setName(request.getName());
        userType.setDescription(request.getDescription());
        userType.setActive(true);
        return userType;
    }

    public void updateEntity(UserType userType, UserTypeUpdateRequest request) {
        userType.setName(request.getName());
        userType.setDescription(request.getDescription());
        userType.setActive(request.getActive());
    }

    public UserTypeResponse toResponse(UserType userType) {
        if (userType == null) {
            return null;
        }

        return new UserTypeResponse(
                userType.getId(),
                userType.getName(),
                userType.getDescription(),
                userType.getActive(),
                userType.getCreatedAt(),
                userType.getUpdatedAt()
        );
    }
}
