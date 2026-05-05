package com.restaurantsystem.restaurantmanagementapi.dto.response;

import com.restaurantsystem.restaurantmanagementapi.enums.TableStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableResponse {

    private Long id;
    private Long restaurantId;
    private Integer tableNumber;
    private Integer seats;
    private TableStatus status;
}
