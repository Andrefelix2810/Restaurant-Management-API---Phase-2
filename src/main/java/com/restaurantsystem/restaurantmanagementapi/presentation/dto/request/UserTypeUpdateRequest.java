package com.restaurantsystem.restaurantmanagementapi.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTypeUpdateRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 80, message = "Name must have between 2 and 80 characters")
    private String name;
}
