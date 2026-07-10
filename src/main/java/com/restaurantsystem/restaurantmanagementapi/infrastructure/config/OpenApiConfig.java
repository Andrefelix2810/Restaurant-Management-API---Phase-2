package com.restaurantsystem.restaurantmanagementapi.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI restaurantManagementApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Restaurant Management API - Phase 2")
                        .description("API for managing users, user types, restaurants and menu items.")
                        .version("2.0.0"));
    }
}
