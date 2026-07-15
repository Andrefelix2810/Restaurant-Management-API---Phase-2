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
public class MenuItemCreateRequest {

    @Schema(example = "Feijoada")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(example = "Feijoada completa com acompanhamentos")
    private String description;

    @Schema(description = "Preco maior que zero", example = "39.90")
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

    @Schema(description = "Indica se o item esta disponivel apenas para consumo no restaurante", example = "true")
    private boolean availableOnlyInRestaurant;

    @Schema(example = "/images/feijoada.jpg")
    private String photoPath;

    @Schema(description = "Id do restaurante ao qual o item pertence", example = "1")
    @NotNull(message = "Restaurant is required")
    private Long restaurantId;
}
