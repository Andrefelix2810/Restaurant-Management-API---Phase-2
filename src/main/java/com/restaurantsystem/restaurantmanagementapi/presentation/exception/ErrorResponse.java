package com.restaurantsystem.restaurantmanagementapi.presentation.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta padronizada para erros da API")
public class ErrorResponse {

    @Schema(example = "2026-07-14T20:30:00")
    private LocalDateTime timestamp;
    @Schema(example = "400")
    private int status;
    @Schema(example = "Bad Request")
    private String error;
    @Schema(example = "Invalid user type. Allowed values: CLIENTE or DONO_RESTAURANTE")
    private String message;
    @Schema(example = "/user-types")
    private String path;
}
