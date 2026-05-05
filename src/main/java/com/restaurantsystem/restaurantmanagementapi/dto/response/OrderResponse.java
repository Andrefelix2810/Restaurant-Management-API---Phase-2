package com.restaurantsystem.restaurantmanagementapi.dto.response;

import com.restaurantsystem.restaurantmanagementapi.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private Long restaurantId;
    private Long tableId;
    private OrderStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
