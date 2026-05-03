package com.restaurantsystem.restaurantmanagementapi.dto.response;

import com.restaurantsystem.restaurantmanagementapi.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    @Schema(description = "Identificador único do usuário", example = "1")
    private Long id;

    @Schema(description = "Nome do usuário", example = "Luis Ricardo")
    private String name;

    @Schema(description = "E-mail do usuário", example = "Luri@Fiap.com")
    private String email;

    @Schema(description = "Login do usuário", example = "lur9")
    private String login;

    @Schema(description = "Data e hora da última atualização do perfil", example = "2026-05-02T10:00:00")
    private LocalDateTime lastModifiedDate;

    @Schema(description = "Tipo de perfil" , example = "CLIENT")
    private Role role;

    @Schema(description = "Dados detalhados do endereço")
    private AddressResponse address;
}