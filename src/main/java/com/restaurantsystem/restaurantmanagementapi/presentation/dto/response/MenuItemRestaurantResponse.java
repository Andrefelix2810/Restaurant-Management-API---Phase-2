package com.restaurantsystem.restaurantmanagementapi.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemRestaurantResponse {

    private Long id;
    private String name;
}
