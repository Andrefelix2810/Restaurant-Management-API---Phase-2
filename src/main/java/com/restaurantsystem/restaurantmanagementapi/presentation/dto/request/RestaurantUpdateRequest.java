package com.restaurantsystem.restaurantmanagementapi.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantUpdateRequest {

    @Schema(example = "Restaurante Sabor Brasil - Centro")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(example = "Rua das Flores, 456 - Sao Paulo/SP")
    @NotBlank(message = "Address is required")
    private String address;

    @Schema(example = "Brasileira")
    @NotBlank(message = "Cuisine type is required")
    private String cuisineType;

    @Schema(example = "Todos os dias, das 11h as 23h")
    @NotBlank(message = "Opening hours is required")
    private String openingHours;

    @Schema(description = "Id de um usuario cujo tipo seja DONO_RESTAURANTE. O usuario inicial possui id 2.", example = "2")
    @NotNull(message = "Owner is required")
    private Long ownerId;
}
