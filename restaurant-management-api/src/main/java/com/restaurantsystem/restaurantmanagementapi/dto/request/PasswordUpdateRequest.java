package com.restaurantsystem.restaurantmanagementapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordUpdateRequest(

        @NotBlank String
        oldPassword,

        @NotBlank @Size(min = 6)
        String newPassword
) {}