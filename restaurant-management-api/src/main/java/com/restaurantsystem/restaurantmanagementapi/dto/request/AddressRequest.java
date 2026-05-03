package com.restaurantsystem.restaurantmanagementapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {

    @NotBlank(message = "Street is required")
    @Schema(description = "Nome da rua ou avenida", example = "Avenida Paulista", requiredMode = Schema.RequiredMode.REQUIRED)
    private String street;

    @NotBlank(message = "Number is required")
    @Schema(description = "Número da residência ou estabelecimento", example = "1000", requiredMode = Schema.RequiredMode.REQUIRED)
    private String number;

    @NotBlank(message = "Neighborhood is required")
    @Schema(description = "Bairro", example = "Bela Vista", requiredMode = Schema.RequiredMode.REQUIRED)
    private String neighborhood;

    @NotBlank(message = "City is required")
    @Schema(description = "Cidade", example = "São Paulo", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @NotBlank(message = "State is required")
    @Schema(description = "Sigla ou nome do Estado", example = "SP", requiredMode = Schema.RequiredMode.REQUIRED)
    private String state;

    @NotBlank(message = "Zip code is required")
    @Schema(description = "CEP (apenas números)", example = "01310100", requiredMode = Schema.RequiredMode.REQUIRED)
    private String zipCode;

    @Schema(description = "Complemento (opcional)", example = "Bloco B, Apto 42")
    private String complement;
}