package com.restaurantsystem.restaurantmanagementapi.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTypeCreateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;
}
