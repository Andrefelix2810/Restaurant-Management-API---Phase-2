package com.restaurantsystem.restaurantmanagementapi.mapper;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.User;
import com.restaurantsystem.restaurantmanagementapi.domain.entity.UserType;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final AddressMapper addressMapper;
    private final UserTypeMapper userTypeMapper;

    public UserMapper(AddressMapper addressMapper, UserTypeMapper userTypeMapper) {
        this.addressMapper = addressMapper;
        this.userTypeMapper = userTypeMapper;
    }

    public User toEntity(UserCreateRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
        user.setAddress(addressMapper.toEntity(request.getAddress()));
        return user;
    }

    public void updateEntity(User user, UserUpdateRequest request, UserType userType) {
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
        user.setUserType(userType);
        user.setAddress(addressMapper.toEntity(request.getAddress()));
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getLastModifiedDate(),
                userTypeMapper.toResponse(user.getUserType()),
                addressMapper.toResponse(user.getAddress())
        );
    }
}
