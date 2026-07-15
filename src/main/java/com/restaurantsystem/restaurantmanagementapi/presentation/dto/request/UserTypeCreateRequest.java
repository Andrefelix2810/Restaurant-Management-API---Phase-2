package com.restaurantsystem.restaurantmanagementapi.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTypeCreateRequest {

    @Schema(
            description = "Tipo de usuario. Cadastre cada opcao apenas uma vez e reutilize o id nos usuarios.",
            example = "CLIENTE",
            allowableValues = {"CLIENTE", "DONO_RESTAURANTE"}
    )
    @NotBlank(message = "Name is required")
    @Pattern(
            regexp = "(?i)^(CLIENTE|DONO_RESTAURANTE)$",
            message = "Invalid user type. Allowed values: CLIENTE or DONO_RESTAURANTE"
    )
    private String name;
}
