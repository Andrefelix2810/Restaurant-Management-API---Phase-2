package com.restaurantsystem.restaurantmanagementapi.dto.request;

import com.restaurantsystem.restaurantmanagementapi.enums.TableStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableCreateRequest {

    @NotNull(message = "Table number is required")
    @Positive(message = "Table number must be greater than zero")
    private Integer tableNumber;

    @NotNull(message = "Seats is required")
    @Positive(message = "Seats must be greater than zero")
    private Integer seats;

    @NotNull(message = "Table status is required")
    private TableStatus status;
}
