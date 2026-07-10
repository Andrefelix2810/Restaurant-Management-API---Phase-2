package com.restaurantsystem.restaurantmanagementapi.mapper;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.Address;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.AddressRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.AddressResponse;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public Address toEntity(AddressRequest request) {
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

    public AddressResponse toResponse(Address address) {
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
