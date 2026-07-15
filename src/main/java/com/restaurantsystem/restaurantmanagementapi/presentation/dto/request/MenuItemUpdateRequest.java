package com.restaurantsystem.restaurantmanagementapi.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemUpdateRequest {

    @Schema(example = "Feijoada completa")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(example = "Feijoada completa para duas pessoas")
    private String description;

    @Schema(description = "Preco maior que zero", example = "49.90")
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

    @Schema(description = "Indica se o item esta disponivel apenas para consumo no restaurante", example = "false")
    private boolean availableOnlyInRestaurant;

    @Schema(example = "/images/feijoada-completa.jpg")
    private String photoPath;

    @Schema(description = "Id do restaurante ao qual o item pertence", example = "1")
    @NotNull(message = "Restaurant is required")
    private Long restaurantId;
}
