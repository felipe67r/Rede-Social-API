package com.redesocial.rede_social_api.controller;

import com.redesocial.rede_social_api.dto.UserRegisterDTO;
import com.redesocial.rede_social_api.dto.UserResponseDTO;
import com.redesocial.rede_social_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuários", description = "Gerenciamento de usuários e autenticação")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar um novo usuário",
            description = "Cria uma nova conta de usuário no sistema com um nome de usuário e email únicos.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de registro inválidos (ex: username/email já existe, validação falha)",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(example = "{\"message\": \"Nome de usuário já existe.\"}")))
            })
    public ResponseEntity<UserResponseDTO> registerUser(
            @Parameter(description = "DTO contendo os dados para registro do usuário (username, password, email)")
            @Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        UserResponseDTO registeredUser = userService.registerUser(userRegisterDTO);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obter todos os usuários",
            description = "Retorna uma lista de todos os usuários registrados no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDTO[].class)))
            })
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter um usuário por ID",
            description = "Retorna os detalhes de um usuário específico pelo seu ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Usuário não encontrado com ID: X\"}")))
            })
    public ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(description = "ID do usuário a ser recuperado")
            @PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um usuário existente",
            description = "Atualiza o email de um usuário específico. O username não pode ser alterado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: email já em uso, validação falha)", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Email já cadastrado.\"}}"))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Usuário não encontrado com ID: X\"}")))
            })
    public ResponseEntity<UserResponseDTO> updateUser(
            @Parameter(description = "ID do usuário a ser atualizado")
            @PathVariable Long id,
            @Parameter(description = "DTO com os dados de atualização do usuário (apenas email permitido para alteração)")
            @Valid @RequestBody UserResponseDTO userUpdateDTO) { // Usando UserResponseDTO para updates
        UserResponseDTO updatedUser = userService.updateUser(id, userUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um usuário",
            description = "Deleta um usuário existente do sistema.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Usuário não encontrado com ID: X\"}")))
            })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID do usuário a ser deletado")
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}