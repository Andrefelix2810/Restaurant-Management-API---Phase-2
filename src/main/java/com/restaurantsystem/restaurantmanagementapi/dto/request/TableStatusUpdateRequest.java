package com.restaurantsystem.restaurantmanagementapi.dto.request;

import com.restaurantsystem.restaurantmanagementapi.enums.TableStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableStatusUpdateRequest {

    @NotNull(message = "Table status is required")
    private TableStatus status;
}
