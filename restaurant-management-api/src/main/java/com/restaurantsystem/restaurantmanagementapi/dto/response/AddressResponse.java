package com.restaurantsystem.restaurantmanagementapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

    @Schema(description = "Nome da rua ou avenida", example = "Avenida Paulista")
    private String street;

    @Schema(description = "Número da residência ou estabelecimento", example = "1000")
    private String number;

    @Schema(description = "Bairro", example = "Bela Vista")
    private String neighborhood;

    @Schema(description = "Cidade", example = "São Paulo")
    private String city;

    @Schema(description = "Sigla ou nome do Estado", example = "SP")
    private String state;

    @Schema(description = "CEP (apenas números)", example = "01310100")
    private String zipCode;

    @Schema(description = "Complemento (opcional)", example = "Bloco B, Apto 42")
    private String complement;
}