package com.restaurantsystem.restaurantmanagementapi.presentation.controller;

import com.restaurantsystem.restaurantmanagementapi.application.port.in.MenuItemUseCase;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.MenuItemCommand;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.MenuItemCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.MenuItemUpdateRequest;
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
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu-items")
@Tag(name = "Menu Items", description = "CRUD dos itens do cardapio")
public class MenuItemController {

    private final MenuItemUseCase menuItemUseCase;

    public MenuItemController(MenuItemUseCase menuItemUseCase) {
        this.menuItemUseCase = menuItemUseCase;
    }

    @PostMapping
    @Operation(summary = "Criar item do cardapio")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item criado"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados invalidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurante nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<MenuItemResponse> create(@Valid @RequestBody MenuItemCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                MenuItemPresenter.toResponse(menuItemUseCase.create(toCommand(request)))
        );
    }

    @GetMapping
    @Operation(summary = "Listar itens do cardapio")
    @ApiResponse(responseCode = "200", description = "Itens de cardapio listados")
    public ResponseEntity<List<MenuItemResponse>> findAll() {
        return ResponseEntity.ok(menuItemUseCase.findAll().stream()
                .map(MenuItemPresenter::toResponse)
                .toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar item do cardapio por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item encontrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<MenuItemResponse> findById(
            @Parameter(description = "Identificador do item", example = "1") @PathVariable Long id
    ) {
        return ResponseEntity.ok(MenuItemPresenter.toResponse(menuItemUseCase.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar item do cardapio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item atualizado"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados invalidos, incluindo preco menor ou igual a zero",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item ou restaurante nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<MenuItemResponse> update(
            @Parameter(description = "Identificador do item", example = "1") @PathVariable Long id,
            @Valid @RequestBody MenuItemUpdateRequest request
    ) {
        return ResponseEntity.ok(MenuItemPresenter.toResponse(
                menuItemUseCase.update(id, toCommand(request))
        ));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir item do cardapio")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item excluido", content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador do item", example = "1") @PathVariable Long id
    ) {
        menuItemUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    private MenuItemCommand toCommand(MenuItemCreateRequest request) {
        return new MenuItemCommand(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.isAvailableOnlyInRestaurant(),
                request.getPhotoPath(),
                request.getRestaurantId()
        );
    }

    private MenuItemCommand toCommand(MenuItemUpdateRequest request) {
        return new MenuItemCommand(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.isAvailableOnlyInRestaurant(),
                request.getPhotoPath(),
                request.getRestaurantId()
        );
    }
}
