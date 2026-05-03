package com.restaurantsystem.restaurantmanagementapi.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @Schema(description = "login ou email do suário", example = "lur9")
        @NotBlank(message = "O login ou email é obrigatório")
        String login,

        @Schema(description = "Senha de acesso do suário", example = "senha123")
        @NotBlank(message = "A senha é obrigatória")
        String password
) {}