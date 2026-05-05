package com.restaurantsystem.restaurantmanagementapi.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {

    @NotNull(message = "Table id is required")
    private Long tableId;

    private String notes;
}
