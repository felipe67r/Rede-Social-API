package com.redesocial.rede_social_api.controller;

import com.redesocial.rede_social_api.dto.FollowDTO;
import com.redesocial.rede_social_api.dto.UserResponseDTO;
import com.redesocial.rede_social_api.service.FollowService;
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
@RequestMapping("/api/follows")
@Tag(name = "Seguidores", description = "Gerenciamento de relações de seguir/parar de seguir entre usuários")
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping
    @Operation(summary = "Fazer um usuário seguir outro",
            description = "Estabelece uma relação de 'seguir' entre dois usuários.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Operação de seguir realizada com sucesso", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: usuário tentando seguir a si mesmo, ou jaá seguindo)", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Usuário não encontrado com ID: X\"}"))),
                    @ApiResponse(responseCode = "404", description = "Usuário seguidor ou seguido não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Usuário não encontrado com ID: X\"}")))
            })
    public ResponseEntity<Void> followUser(
            @Parameter(description = "DTO contendo os IDs do seguidor e do seguido")
            @Valid @RequestBody FollowDTO followDTO) {
        followService.followUser(followDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Operation(summary = "Parar de seguir um usuário",
            description = "Remove uma relação de 'seguir' existente entre dois usuários.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Operação de parar de seguir realizada com sucesso", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: tentativa de parar de seguir quem não está seguindo)", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Você não está seguindo este usuário.\"}"))),
                    @ApiResponse(responseCode = "404", description = "Usuário seguidor ou seguido não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Usuário não encontrado com ID: X\"}")))
            })
    public ResponseEntity<Void> unfollowUser(
            @Parameter(description = "DTO contendo os IDs do seguidor e do seguido")
            @Valid @RequestBody FollowDTO followDTO) {
        followService.unfollowUser(followDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/followers/{userId}")
    @Operation(summary = "Obter lista de seguidores de um usuário",
            description = "Retorna uma lista de usuários que seguem o ID de usuário especificado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de seguidores retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDTO[].class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Usuário não encontrado com ID: X\"}")))
            })
    public ResponseEntity<List<UserResponseDTO>> getFollowers(
            @Parameter(description = "ID do usuário para buscar os seguidores")
            @PathVariable Long userId) {
        List<UserResponseDTO> followers = followService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/following/{userId}")
    @Operation(summary = "Obter lista de usuários que um usuário segue",
            description = "Retorna uma lista de usuários que o ID de usuário especificado está seguindo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de usuários que o usuário segue retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDTO[].class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Usuário não encontrado com ID: X\"}")))
            })
    public ResponseEntity<List<UserResponseDTO>> getFollowing(
            @Parameter(description = "ID do usuário para buscar quem ele está seguindo")
            @PathVariable Long userId) {
        List<UserResponseDTO> following = followService.getFollowing(userId);
        return ResponseEntity.ok(following);
    }

    @GetMapping("/followers/count/{userId}")
    @Operation(summary = "Contar o número de seguidores de um usuário",
            description = "Retorna a contagem total de seguidores para o ID de usuário especificado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contagem de seguidores retornada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(type = "integer", format = "int64", example = "10"))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Usuário não encontrado com ID: X\"}")))
            })
    public ResponseEntity<Long> countFollowers(
            @Parameter(description = "ID do usuário para contar os seguidores")
            @PathVariable Long userId) {
        long count = followService.countFollowers(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/following/count/{userId}")
    @Operation(summary = "Contar o número de usuários que um usuário segue",
            description = "Retorna a contagem total de usuários que o ID de usuário especificado está seguindo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Contagem de usuários que o usuário segue retornada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(type = "integer", format = "int64", example = "5"))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Usuário não encontrado com ID: X\"}")))
            })
    public ResponseEntity<Long> countFollowing(
            @Parameter(description = "ID do usuário para contar quem ele está seguindo")
            @PathVariable Long userId) {
        long count = followService.countFollowing(userId);
        return ResponseEntity.ok(count);
    }
}