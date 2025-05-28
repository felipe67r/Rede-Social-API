package com.redesocial.rede_social_api.controller;

import com.redesocial.rede_social_api.dto.LikeDTO;
import com.redesocial.rede_social_api.service.LikeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/likes")
@Tag(name = "Curtidas", description = "Gerenciamento de curtidas em posts")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    @Operation(summary = "Curtir um post",
            description = "Registra uma curtida de um usuário em um post.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Post curtido com sucesso", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: usuário já curtiu o post)", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Você já curtiu este post.\"}"))),
                    @ApiResponse(responseCode = "404", description = "Usuário ou post não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Usuário não encontrado com ID: X\"}")))
            })
    public ResponseEntity<Void> likePost(
            @Parameter(description = "DTO contendo os IDs do usuário e do post")
            @Valid @RequestBody LikeDTO likeDTO) {
        likeService.likePost(likeDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Operation(summary = "Descurtir um post",
            description = "Remove a curtida de um usuário em um post.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Post descurtido com sucesso", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: usuário não curtiu o post)", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Você não curtiu este post.\"}"))),
                    @ApiResponse(responseCode = "404", description = "Usuário ou post não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Post não encontrado com ID: X\"}")))
            })
    public ResponseEntity<Void> unlikePost(
            @Parameter(description = "DTO contendo os IDs do usuário e do post")
            @Valid @RequestBody LikeDTO likeDTO) {
        likeService.unlikePost(likeDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}