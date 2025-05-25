package com.redesocial.rede_social_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentCreateDTO {

    @NotNull(message = "ID do post é obrigatório")
    private Long postId;

    @NotBlank(message = "Comentário não pode ser vazio")
    @Size(max = 250, message = "Comentário deve ter no máximo 250 caracteres")
    private String content;
}