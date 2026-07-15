package com.restaurantsystem.restaurantmanagementapi.application.port.in.command;

public record AddressCommand(
        String street,
        String number,
        String neighborhood,
        String city,
        String state,
        String zipCode,
        String complement
) {
}
