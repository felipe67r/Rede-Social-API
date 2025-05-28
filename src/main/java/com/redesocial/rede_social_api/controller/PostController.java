package com.redesocial.rede_social_api.controller;

import com.redesocial.rede_social_api.dto.PostCreateDTO;
import com.redesocial.rede_social_api.dto.PostResponseDTO;
import com.redesocial.rede_social_api.service.PostService;
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
@RequestMapping("/api/posts")
@Tag(name = "Posts", description = "Gerenciamento de posts de usuários")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @Operation(summary = "Criar um novo post",
            description = "Cria um novo post para um usuário específico.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Post criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PostResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: conteúdo vazio)", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"O conteúdo do post não pode ser vazio\"}"))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Usuário não encontrado com ID: X\"}")))
            })
    public ResponseEntity<PostResponseDTO> createPost(
            @Parameter(description = "DTO para criação do post")
            @Valid @RequestBody PostCreateDTO postCreateDTO,
            @Parameter(description = "ID do usuário criador do post")
            @RequestParam Long userId) {
        PostResponseDTO createdPost = postService.createPost(postCreateDTO, userId);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obter todos os posts",
            description = "Retorna uma lista de todos os posts existentes no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de posts retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PostResponseDTO[].class)))
            })
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<PostResponseDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter um post por ID",
            description = "Retorna os detalhes de um post específico pelo seu ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Post encontrado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PostResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Post não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Post não encontrado com ID: X\"}")))
            })
    public ResponseEntity<PostResponseDTO> getPostById(
            @Parameter(description = "ID do post a ser recuperado")
            @PathVariable Long id) {
        PostResponseDTO post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um post existente",
            description = "Atualiza o conteúdo de um post específico. Apenas o criador do post pode atualizá-lo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Post atualizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PostResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: conteúdo vazio, usuário não autorizado)", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Não autorizado a atualizar este post.\"}}"))),
                    @ApiResponse(responseCode = "404", description = "Post ou usuário não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Post não encontrado com ID: X\"}")))
            })
    public ResponseEntity<PostResponseDTO> updatePost(
            @Parameter(description = "ID do post a ser atualizado")
            @PathVariable Long id,
            @Parameter(description = "DTO com os dados de atualização do post")
            @Valid @RequestBody PostCreateDTO postUpdateDTO,
            @Parameter(description = "ID do usuário que está tentando atualizar o post")
            @RequestParam Long userId) {
        PostResponseDTO updatedPost = postService.updatePost(id, postUpdateDTO, userId);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um post",
            description = "Deleta um post existente. Apenas o criador do post pode deletá-lo.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Post deletado com sucesso", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: usuário não autorizado)", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Não autorizado a deletar este post.\"}}"))),
                    @ApiResponse(responseCode = "404", description = "Post ou usuário não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Post não encontrado com ID: X\"}")))
            })
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "ID do post a ser deletado")
            @PathVariable Long id,
            @Parameter(description = "ID do usuário que está tentando deletar o post")
            @RequestParam Long userId) {
        postService.deletePost(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obter posts de um usuário específico",
            description = "Retorna todos os posts criados por um usuário com o ID especificado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Posts do usuário retornados com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PostResponseDTO[].class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Usuário não encontrado com ID: X\"}")))
            })
    public ResponseEntity<List<PostResponseDTO>> getPostsByUserId(
            @Parameter(description = "ID do usuário para buscar os posts")
            @PathVariable Long userId) {
        List<PostResponseDTO> posts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }
}