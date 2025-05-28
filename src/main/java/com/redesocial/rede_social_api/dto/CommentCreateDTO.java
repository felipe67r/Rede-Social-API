package com.redesocial.rede_social_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "DTO para a criação de um novo comentário")
public class CommentCreateDTO {

    @NotNull(message = "ID do post é obrigatório")
    @Schema(description = "ID do post ao qual o comentário pertence", example = "1")
    private Long postId;

    @NotBlank(message = "Comentário não pode ser vazio")
    @Size(max = 250, message = "Comentário deve ter no máximo 250 caracteres")
    @Schema(description = "Conteúdo do comentário", example = "Este é um ótimo post!")
    private String content;
}