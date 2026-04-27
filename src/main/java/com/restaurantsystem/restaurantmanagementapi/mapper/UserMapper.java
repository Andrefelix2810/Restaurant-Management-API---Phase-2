package com.restaurantsystem.restaurantmanagementapi.mapper;

import com.restaurantsystem.restaurantmanagementapi.dto.request.AddressRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.request.UserCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.response.AddressResponse;
import com.restaurantsystem.restaurantmanagementapi.dto.response.UserResponse;
import com.restaurantsystem.restaurantmanagementapi.entity.Address;
import com.restaurantsystem.restaurantmanagementapi.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserCreateRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        user.setAddress(toAddressEntity(request.getAddress()));
        return user;
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getLastModifiedDate(),
                user.getRole(),
                toAddressResponse(user.getAddress())
        );
    }

    public Address toAddressEntity(AddressRequest request) {
        if (request == null) {
            return null;
        }

        return new Address(
                request.getStreet(),
                request.getNumber(),
                request.getNeighborhood(),
                request.getCity(),
                request.getState(),
                request.getZipCode(),
                request.getComplement()
        );
    }

    public AddressResponse toAddressResponse(Address address) {
        if (address == null) {
            return null;
        }

        return new AddressResponse(
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