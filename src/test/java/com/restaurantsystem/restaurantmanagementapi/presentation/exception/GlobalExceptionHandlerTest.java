package com.restaurantsystem.restaurantmanagementapi.presentation.exception;

import com.restaurantsystem.restaurantmanagementapi.domain.exception.BusinessException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest("GET", "/test");
    }

    @Test
    void shouldHandleBusinessException() {
        ResponseEntity<ErrorResponse> response = handler.handleBusinessException(
                new BusinessException("Business rule"), request
        );

        assertResponse(response, HttpStatus.BAD_REQUEST, "Business rule");
    }

    @Test
    void shouldHandleInvalidPathVariableType() {
        MethodArgumentTypeMismatchException exception = new MethodArgumentTypeMismatchException(
                "invalid", Long.class, "id", null, null
        );

        ResponseEntity<ErrorResponse> response = handler.handleMethodArgumentTypeMismatch(exception, request);

        assertResponse(response, HttpStatus.BAD_REQUEST, "Invalid value for parameter: id");
    }

    @Test
    void shouldHandleConstraintViolation() {
        ResponseEntity<ErrorResponse> response = handler.handleConstraintViolation(
                new ConstraintViolationException("Invalid constraint", null), request
        );

        assertResponse(response, HttpStatus.BAD_REQUEST, "Invalid constraint");
    }

    @Test
    void shouldHandleUnknownEndpoint() {
        ResponseEntity<ErrorResponse> response = handler.handleEndpointNotFound(
                new NoResourceFoundException(HttpMethod.GET, "/unknown"), request
        );

        assertResponse(response, HttpStatus.NOT_FOUND, "Endpoint not found");
    }

    @Test
    void shouldHandleDataIntegrityViolationWithoutExposingDatabaseDetails() {
        ResponseEntity<ErrorResponse> response = handler.handleDataIntegrityViolation(
                new DataIntegrityViolationException("sensitive database details"), request
        );

        assertResponse(response, HttpStatus.CONFLICT, "Data integrity violation");
    }

    @Test
    void shouldHandleUnexpectedExceptionWithoutExposingInternalDetails() {
        ResponseEntity<ErrorResponse> response = handler.handleGenericError(
                new RuntimeException("sensitive internal details"), request
        );

        assertResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected internal server error");
    }

    private void assertResponse(ResponseEntity<ErrorResponse> response, HttpStatus status, String message) {
        assertEquals(status, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(status.value(), response.getBody().getStatus());
        assertEquals(message, response.getBody().getMessage());
        assertEquals("/test", response.getBody().getPath());
    }
}
