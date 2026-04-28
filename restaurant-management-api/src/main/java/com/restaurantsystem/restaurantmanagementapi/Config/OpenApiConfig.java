package com.restaurantsystem.restaurantmanagementapi.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Restaurant Management API")
                        .version("1.0")
                        .description("API para gestão de restaurantes e usuários.")
                        .contact(new Contact()
                                .name("André Félix e Ygor")
                                .email("seu-email@exemplo.com")));
    }
}