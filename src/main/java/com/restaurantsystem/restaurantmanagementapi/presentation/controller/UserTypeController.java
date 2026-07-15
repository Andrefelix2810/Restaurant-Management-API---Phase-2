package com.restaurantsystem.restaurantmanagementapi.presentation.controller;

import com.restaurantsystem.restaurantmanagementapi.application.port.in.UserTypeUseCase;
import com.restaurantsystem.restaurantmanagementapi.application.port.in.command.UserTypeCommand;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserTypeCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.request.UserTypeUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.presentation.dto.response.UserTypeResponse;
import com.restaurantsystem.restaurantmanagementapi.presentation.exception.ErrorResponse;
import com.restaurantsystem.restaurantmanagementapi.presentation.presenter.UserTypePresenter;
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
@RequestMapping("/user-types")
@Tag(
        name = "Tipos de usuario",
        description = "Catalogo fechado e reutilizavel: id 1 = CLIENTE; id 2 = DONO_RESTAURANTE."
)
public class UserTypeController {

    private final UserTypeUseCase userTypeUseCase;

    public UserTypeController(UserTypeUseCase userTypeUseCase) {
        this.userTypeUseCase = userTypeUseCase;
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar um tipo permitido",
            description = "A base ja cria os dois tipos. Aceita somente CLIENTE ou DONO_RESTAURANTE quando o registro nao existir; caso contrario, consulte GET /user-types e reutilize o id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tipo de usuario criado"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Valor diferente dos dois tipos permitidos ou tipo ja cadastrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<UserTypeResponse> create(@Valid @RequestBody UserTypeCreateRequest request) {
        UserTypeResponse response = UserTypePresenter.toResponse(
                userTypeUseCase.create(new UserTypeCommand(request.getName()))
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(
            summary = "Listar os tipos cadastrados",
            description = "Retorna CLIENTE no id 1 e DONO_RESTAURANTE no id 2. Reutilize esses ids no campo userTypeId de quantos usuarios forem necessarios."
    )
    @ApiResponse(responseCode = "200", description = "Tipos de usuario listados")
    public ResponseEntity<List<UserTypeResponse>> findAll() {
        return ResponseEntity.ok(userTypeUseCase.findAll().stream()
                .map(UserTypePresenter::toResponse)
                .toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de usuario por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de usuario encontrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tipo de usuario nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<UserTypeResponse> findById(
            @Parameter(description = "Identificador do tipo de usuario", example = "1") @PathVariable Long id
    ) {
        return ResponseEntity.ok(UserTypePresenter.toResponse(userTypeUseCase.findById(id)));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar tipo de usuario",
            description = "O novo nome deve ser CLIENTE ou DONO_RESTAURANTE. Um tipo compartilhado por usuarios nao pode ser renomeado; para trocar o perfil de uma pessoa, atualize o userTypeId em PUT /users/{id}."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de usuario atualizado"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Tipo invalido, nome ja cadastrado ou tipo compartilhado por usuarios",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tipo de usuario nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<UserTypeResponse> update(
            @Parameter(description = "Identificador do tipo de usuario", example = "1") @PathVariable Long id,
            @Valid @RequestBody UserTypeUpdateRequest request
    ) {
        return ResponseEntity.ok(UserTypePresenter.toResponse(
                userTypeUseCase.update(id, new UserTypeCommand(request.getName()))
        ));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir tipo de usuario",
            description = "A exclusao so e permitida quando nenhum usuario estiver reutilizando o tipo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tipo de usuario excluido", content = @Content),
            @ApiResponse(
                    responseCode = "400",
                    description = "Tipo associado a um ou mais usuarios",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tipo de usuario nao encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador do tipo de usuario", example = "1") @PathVariable Long id
    ) {
        userTypeUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
