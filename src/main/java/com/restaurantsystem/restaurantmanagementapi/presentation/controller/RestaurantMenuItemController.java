package com.restaurantsystem.restaurantmanagementapi.presentation.controller;

import com.restaurantsystem.restaurantmanagementapi.application.port.in.MenuItemUseCase;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.MenuItemResponse;
import com.restaurantsystem.restaurantmanagementapi.presentation.exception.ErrorResponse;
import com.restaurantsystem.restaurantmanagementapi.presentation.presenter.MenuItemPresenter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurants/{restaurantId}/menu-items")
@Tag(name = "Restaurant Menu", description = "Consulta do cardapio por restaurante")
public class RestaurantMenuItemController {

    private final MenuItemUseCase menuItemUseCase;

    public RestaurantMenuItemController(MenuItemUseCase menuItemUseCase) {
        this.menuItemUseCase = menuItemUseCase;
    }

    @GetMapping
    @Operation(summary = "Listar itens de um restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Itens do restaurante listados"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurante nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<MenuItemResponse>> findByRestaurantId(
            @Parameter(description = "Identificador do restaurante", example = "1")
            @PathVariable Long restaurantId
    ) {
        return ResponseEntity.ok(menuItemUseCase.findByRestaurantId(restaurantId).stream()
                .map(MenuItemPresenter::toResponse)
                .toList());
    }
}
