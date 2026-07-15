package com.restaurantsystem.restaurantmanagementapi.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Saude", description = "Verificacao de disponibilidade da API")
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "Verificar se a API esta disponivel")
    @ApiResponse(responseCode = "200", description = "Aplicacao disponivel")
    public Map<String, String> health() {
        return Map.of(
                "status", "UP",
                "application", "Restaurant Management API - Phase 2"
        );
    }
}
