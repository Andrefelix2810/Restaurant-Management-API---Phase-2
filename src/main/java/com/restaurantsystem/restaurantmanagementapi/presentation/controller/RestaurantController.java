package com.restaurantsystem.restaurantmanagementapi.presentation.controller;

import com.restaurantsystem.restaurantmanagementapi.application.port.in.RestaurantUseCase;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.RestaurantCommand;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.RestaurantCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.RestaurantUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.RestaurantResponse;
import com.restaurantsystem.restaurantmanagementapi.presentation.exception.ErrorResponse;
import com.restaurantsystem.restaurantmanagementapi.presentation.presenter.RestaurantPresenter;
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
@RequestMapping("/restaurants")
@Tag(name = "Restaurants", description = "CRUD dos restaurantes")
public class RestaurantController {

    private final RestaurantUseCase restaurantUseCase;

    public RestaurantController(RestaurantUseCase restaurantUseCase) {
        this.restaurantUseCase = restaurantUseCase;
    }

    @PostMapping
    @Operation(summary = "Criar restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurante criado"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados invalidos ou usuario sem perfil de dono",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Dono nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<RestaurantResponse> create(@Valid @RequestBody RestaurantCreateRequest request) {
        RestaurantResponse response = RestaurantPresenter.toResponse(
                restaurantUseCase.create(toCommand(request))
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar restaurantes")
    @ApiResponse(responseCode = "200", description = "Restaurantes listados")
    public ResponseEntity<List<RestaurantResponse>> findAll() {
        return ResponseEntity.ok(restaurantUseCase.findAll().stream()
                .map(RestaurantPresenter::toResponse)
                .toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar restaurante por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurante nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<RestaurantResponse> findById(
            @Parameter(description = "Identificador do restaurante", example = "1") @PathVariable Long id
    ) {
        return ResponseEntity.ok(RestaurantPresenter.toResponse(restaurantUseCase.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante atualizado"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados invalidos ou usuario sem tipo DONO_RESTAURANTE",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurante ou dono nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<RestaurantResponse> update(
            @Parameter(description = "Identificador do restaurante", example = "1") @PathVariable Long id,
            @Valid @RequestBody RestaurantUpdateRequest request
    ) {
        return ResponseEntity.ok(RestaurantPresenter.toResponse(
                restaurantUseCase.update(id, toCommand(request))
        ));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir restaurante")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Restaurante excluido", content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurante nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Restaurante ainda possui itens de cardapio vinculados",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador do restaurante", example = "1") @PathVariable Long id
    ) {
        restaurantUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    private RestaurantCommand toCommand(RestaurantCreateRequest request) {
        return new RestaurantCommand(
                request.getName(),
                request.getAddress(),
                request.getCuisineType(),
                request.getOpeningHours(),
                request.getOwnerId()
        );
    }

    private RestaurantCommand toCommand(RestaurantUpdateRequest request) {
        return new RestaurantCommand(
                request.getName(),
                request.getAddress(),
                request.getCuisineType(),
                request.getOpeningHours(),
                request.getOwnerId()
        );
    }
}
