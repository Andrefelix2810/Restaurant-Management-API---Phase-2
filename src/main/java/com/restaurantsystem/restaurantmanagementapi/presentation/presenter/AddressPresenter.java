package com.restaurantsystem.restaurantmanagementapi.presentation.presenter;

import com.restaurantsystem.restaurantmanagementapi.domain.entity.Address;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.AddressResponse;

public final class AddressPresenter {

    private AddressPresenter() {
    }

    public static AddressResponse toResponse(Address address) {
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
