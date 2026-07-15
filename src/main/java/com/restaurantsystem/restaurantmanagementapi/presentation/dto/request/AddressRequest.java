package com.restaurantsystem.restaurantmanagementapi.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {

    @Schema(example = "Rua Central")
    private String street;
    @Schema(example = "100")
    private String number;
    @Schema(example = "Centro")
    private String neighborhood;
    @Schema(example = "Sao Paulo")
    private String city;
    @Schema(example = "SP")
    private String state;
    @Schema(example = "01001000")
    private String zipCode;
    @Schema(example = "Apartamento 12")
    private String complement;
}
