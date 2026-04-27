package com.restaurantsystem.restaurantmanagementapi.dto.response;

import com.restaurantsystem.restaurantmanagementapi.enums.Role;
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
    private Role role;
    private AddressResponse address;
}