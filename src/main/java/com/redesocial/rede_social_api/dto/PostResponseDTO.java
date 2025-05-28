package com.redesocial.rede_social_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para a resposta de um post")
public class PostResponseDTO {
    @Schema(description = "ID único do post", example = "50", accessMode = AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Conteúdo do post", example = "Que dia lindo para aprender sobre documentação de API!")
    private String content;

    @Schema(description = "ID do usuário que criou o post", example = "1", accessMode = AccessMode.READ_ONLY)
    private Long userId;

    @Schema(description = "Nome de usuário do autor do post", example = "user_test", accessMode = AccessMode.READ_ONLY)
    private String username;

    @Schema(description = "Data e hora de criação do post", example = "2024-05-28T11:00:00", accessMode = AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
}