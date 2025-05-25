package com.redesocial.rede_social_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class PostCreateDTO {

    @NotBlank(message = "O conteúdo do post é obrigatório")
    @Size(max = 280, message = "O post deve ter no máximo 280 caracteres")
    private String content;

}