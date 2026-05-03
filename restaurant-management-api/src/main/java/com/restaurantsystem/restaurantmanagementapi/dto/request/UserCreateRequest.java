package com.restaurantsystem.restaurantmanagementapi.dto.request;

import com.restaurantsystem.restaurantmanagementapi.enums.Role;
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

    @NotBlank(message = "Name is required")
    @Schema(description = "Nome do usuário", example = "Luis Ricardo", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(description = "E-mail do usuário", example = "Luri@Fiap.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Login is required")
    @Schema(description = "Login do usuário", example = "lur9", requiredMode = Schema.RequiredMode.REQUIRED)
    private String login;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must have at least 6 characters")
    @Schema(description = "Senha com no mínimo 6 caracteres", example = "senha123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Valid
    @NotNull(message = "Address is required")
    @Schema(description = "Dados de endereço do usuário")
    private AddressRequest address;
}