package com.restaurantsystem.restaurantmanagementapi.application.port.in.command;

public record UserCommand(
        String name,
        String email,
        String login,
        String password,
        Long userTypeId,
        AddressCommand address
) {
}
