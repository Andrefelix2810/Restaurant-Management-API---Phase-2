package com.restaurantsystem.restaurantmanagementapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public record PasswordUpdateRequest(

        @Schema(description = "Senha atual do usuário para validação de segurança", example = "Edson123")
        @NotBlank String
        oldPassword,

        @Schema(description = "Nova senha a ser definida (mínimo 6 caracteres)", example = "Edinho123")
        @NotBlank @Size(min = 6)
        String newPassword
) {}