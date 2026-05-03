package com.restaurantsystem.restaurantmanagementapi.dto.request;

import com.restaurantsystem.restaurantmanagementapi.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "Name is required")
    @Schema(description = "Nome do usuário para atualização", example = "Edson Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(description = "E-mail do usuário para atualização", example = "edson@fiap.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Login is required")
    @Schema(description = "Login do usuário para atualização", example = "edson123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String login;

    @Valid
    @NotNull(message = "Address is required")
    @Schema(description = "Dados de endereço atualizados")
    private AddressRequest address;
}