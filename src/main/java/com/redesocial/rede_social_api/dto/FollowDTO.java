package com.redesocial.rede_social_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "DTO para realizar operações de seguir/parar de seguir usuários")
public class FollowDTO {

    @NotNull(message = "O ID do seguidor não pode ser nulo.")
    @Schema(description = "ID do usuário que está iniciando a ação de seguir/parar de seguir", example = "1")
    private Long followerId;

    @NotNull(message = "O ID do usuário a ser seguido não pode ser nulo.")
    @Schema(description = "ID do usuário que está sendo seguido/deixado de seguir", example = "2")
    private Long followedId;

}