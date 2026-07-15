package com.restaurantsystem.restaurantmanagementapi.presentation.controller;

import com.restaurantsystem.restaurantmanagementapi.application.port.in.UserUseCase;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.AddressCommand;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.UserCommand;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.AddressRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.UserResponse;
import com.restaurantsystem.restaurantmanagementapi.presentation.exception.ErrorResponse;
import com.restaurantsystem.restaurantmanagementapi.presentation.presenter.UserPresenter;
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
@RequestMapping("/users")
@Tag(name = "Usuarios", description = "CRUD de usuarios vinculados a um tipo reutilizavel")
public class UserController {

    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @PostMapping
    @Operation(
            summary = "Criar usuario",
            description = "Informe userTypeId 1 para CLIENTE ou 2 para DONO_RESTAURANTE. O mesmo id pode ser usado por varios usuarios."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario criado"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados invalidos, email ou login ja cadastrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tipo de usuario nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = UserPresenter.toResponse(userUseCase.create(toCommand(request)));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar usuarios")
    @ApiResponse(responseCode = "200", description = "Usuarios listados")
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userUseCase.findAll().stream()
                .map(UserPresenter::toResponse)
                .toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<UserResponse> findById(
            @Parameter(description = "Identificador do usuario", example = "1") @PathVariable Long id
    ) {
        return ResponseEntity.ok(UserPresenter.toResponse(userUseCase.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuario e seu tipo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario atualizado"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados invalidos, email ou login ja utilizado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario ou tipo de usuario nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<UserResponse> update(
            @Parameter(description = "Identificador do usuario", example = "1") @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        return ResponseEntity.ok(UserPresenter.toResponse(userUseCase.update(id, toCommand(request))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario excluido", content = @Content),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Usuario ainda vinculado como dono de restaurante",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador do usuario", example = "1") @PathVariable Long id
    ) {
        userUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    private UserCommand toCommand(UserCreateRequest request) {
        return new UserCommand(
                request.getName(),
                request.getEmail(),
                request.getLogin(),
                request.getPassword(),
                request.getUserTypeId(),
                toAddressCommand(request.getAddress())
        );
    }

    private UserCommand toCommand(UserUpdateRequest request) {
        return new UserCommand(
                request.getName(),
                request.getEmail(),
                request.getLogin(),
                request.getPassword(),
                request.getUserTypeId(),
                toAddressCommand(request.getAddress())
        );
    }

    private AddressCommand toAddressCommand(AddressRequest address) {
        if (address == null) {
            return null;
        }
        return new AddressCommand(
                address.getStreet(),
                address.getNumber(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getZipCode(),
                address.getComplement()
        );
    }
}
