package com.redesocial.rede_social_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "DTO para a criação ou atualização de um post")
public class PostCreateDTO {

    @NotBlank(message = "O conteúdo do post é obrigatório")
    @Size(max = 280, message = "O post deve ter no máximo 280 caracteres")
    @Schema(description = "Conteúdo textual do post", example = "Minha primeira publicação na rede social!")
    private String content;

}