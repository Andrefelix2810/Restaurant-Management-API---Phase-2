package com.restaurantsystem.restaurantmanagementapi.dto.request;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "O login ou email é obrigatório")
        String login,

        @NotBlank(message = "A senha é obrigatória")
        String password
) {}