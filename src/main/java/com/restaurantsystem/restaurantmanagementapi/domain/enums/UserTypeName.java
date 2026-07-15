package com.restaurantsystem.restaurantmanagementapi.domain.enums;

import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;

import java.util.Arrays;
import java.util.Locale;

public enum UserTypeName {

    CLIENTE,
    DONO_RESTAURANTE;

    public static UserTypeName from(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException("Name is required");
        }

        String normalizedValue = value.trim().toUpperCase(Locale.ROOT);
        return Arrays.stream(values())
                .filter(type -> type.name().equals(normalizedValue))
                .findFirst()
                .orElseThrow(() -> new BusinessException(
                        "Invalid user type. Allowed values: CLIENTE or DONO_RESTAURANTE"
                ));
    }
}
