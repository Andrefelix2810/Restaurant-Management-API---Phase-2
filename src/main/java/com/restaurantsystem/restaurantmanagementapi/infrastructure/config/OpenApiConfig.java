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
                        .description("""
                                API para gerenciamento de usuarios, restaurantes e itens de cardapio.

                                Fluxo de tipos de usuario:
                                1. A base inicia com CLIENTE no id 1 e DONO_RESTAURANTE no id 2.
                                2. Consulte GET /user-types para visualizar o catalogo reutilizavel.
                                3. Reutilize userTypeId 1 ou 2 no cadastro de qualquer quantidade de usuarios.
                                4. A base vazia recebe um usuario de exemplo de cada tipo.
                                5. Somente um usuario vinculado a DONO_RESTAURANTE pode ser dono de restaurante.
                                """)
                        .version("2.1.0"));
    }
}
