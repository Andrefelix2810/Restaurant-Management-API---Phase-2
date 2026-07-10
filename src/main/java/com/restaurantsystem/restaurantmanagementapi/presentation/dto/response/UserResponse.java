package com.restaurantsystem.restaurantmanagementapi.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String login;
    private LocalDateTime lastModifiedDate;
    private UserTypeResponse userType;
    private AddressResponse address;
}
