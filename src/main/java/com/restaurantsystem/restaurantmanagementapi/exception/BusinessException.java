package com.restaurantsystem.restaurantmanagementapi.exception;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}