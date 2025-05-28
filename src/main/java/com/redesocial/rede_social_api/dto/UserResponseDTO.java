package com.redesocial.rede_social_api.dto;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;

@Data
@Schema(description = "DTO para a resposta de informações de um usuário")
public class UserResponseDTO {
    @Schema(description = "ID único do usuário", example = "1", accessMode = AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nome de usuário único", example = "joao_silva")
    private String username;

    @Schema(description = "Endereço de email do usuário", example = "joao.silva@example.com")
    private String email;
}