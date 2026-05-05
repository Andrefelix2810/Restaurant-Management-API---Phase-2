package com.restaurantsystem.restaurantmanagementapi.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemUpdateRequest {

    @NotBlank(message = "Menu item name is required")
    private String name;

    private String description;

    @NotNull(message = "Menu item price is required")
    @DecimalMin(value = "0.01", message = "Menu item price must be greater than zero")
    private BigDecimal price;

    @NotNull(message = "Availability is required")
    private Boolean available;
}
