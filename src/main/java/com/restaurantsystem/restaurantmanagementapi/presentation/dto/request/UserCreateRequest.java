package com.restaurantsystem.restaurantmanagementapi.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    @Schema(description = "Nome completo", example = "Maria Silva")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "E-mail unico", example = "maria@email.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Schema(description = "Login unico", example = "maria.silva")
    @NotBlank(message = "Login is required")
    private String login;

    @Schema(description = "Senha com pelo menos 6 caracteres", example = "123456", format = "password", accessMode = Schema.AccessMode.WRITE_ONLY)
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;

    @Schema(
            description = "Id do tipo reutilizavel: 1 = CLIENTE; 2 = DONO_RESTAURANTE.",
            example = "1"
    )
    @NotNull(message = "User type is required")
    private Long userTypeId;

    @Schema(description = "Endereco completo do usuario")
    @Valid
    @NotNull(message = "Address is required")
    private AddressRequest address;
}
