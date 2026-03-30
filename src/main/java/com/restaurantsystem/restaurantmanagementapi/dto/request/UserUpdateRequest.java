package com.restaurantsystem.restaurantmanagementapi.dto.request;

import com.restaurantsystem.restaurantmanagementapi.enums.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Login is required")
    private String login;

    @NotNull(message = "Role is required")
    private Role role;

    @Valid
    @NotNull(message = "Address is required")
    private AddressRequest address;
}