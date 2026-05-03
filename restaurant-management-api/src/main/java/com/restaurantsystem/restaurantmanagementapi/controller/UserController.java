package com.restaurantsystem.restaurantmanagementapi.controller;

import com.restaurantsystem.restaurantmanagementapi.dto.request.PasswordUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.request.UserCreateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.request.UserUpdateRequest;
import com.restaurantsystem.restaurantmanagementapi.dto.response.UserResponse;
import com.restaurantsystem.restaurantmanagementapi.enums.Role;
import com.restaurantsystem.restaurantmanagementapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "Gestão de perfil e conta de  usuários")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register/client")
    @Operation(summary = "Registrar Cliente", description = "Cria um usuário com perfil de CLIENTE")
    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos, e-mail ou Login já cadastrado")
    public ResponseEntity<UserResponse> registerClient(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = userService.create(request, Role.CLIENT);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/register/owner")
    @Operation(summary = "Registrar Dono", description = "Cria um usuário com perfil de OWNER")
    @ApiResponse(responseCode = "201", description = "Dono criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos, e-mail ou Login já cadastrado")
    public ResponseEntity<UserResponse> registerOwner(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = userService.create(request, Role.RESTAURANT_OWNER);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Buscar todos usuários", description = "Buscar todos os usuários cadastrado na base de dados ")
    @ApiResponse(responseCode = "200", description = "Lista de usuários recuperada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno ao processar a listagem")
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Buscar usuário por ID no sistema")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado na base de dados")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados do usuário", description = "Atualizar um determinado campo do usuário com excecao da senha")
    @ApiResponse(responseCode = "200", description = "Dados do usuário atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado na base de dados")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @PatchMapping("/{id}/password")
    @Operation(summary = "Atualizar senha", description = "Altera a senha de um usuário específico")
    @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso")
    @ApiResponse(responseCode = "400", description = "Senha antiga não confere ou nova senha inválida")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody PasswordUpdateRequest request) {
        userService.updatePassword(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir usuário", description = "Excluir um usuário no sistema passando o ID")
    @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}