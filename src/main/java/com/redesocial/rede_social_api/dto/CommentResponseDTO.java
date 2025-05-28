package com.redesocial.rede_social_api.dto;

import java.time.LocalDateTime;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;

@Data
@Schema(description = "DTO para a resposta de um comentário")
public class CommentResponseDTO {
    @Schema(description = "ID único do comentário", example = "101", accessMode = AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Conteúdo do comentário", example = "Excelente conteúdo, muito útil!")
    private String content;

    @Schema(description = "Nome de usuário do autor do comentário", example = "user_exemplo", accessMode = AccessMode.READ_ONLY)
    private String username;

    @Schema(description = "ID do post ao qual o comentário está associado", example = "1", accessMode = AccessMode.READ_ONLY)
    private Long postId;

    @Schema(description = "Data e hora de criação do comentário", example = "2024-05-28T10:30:00", accessMode = AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
}