package com.redesocial.rede_social_api.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "DTO para realizar operações de curtir/descurtir posts")
public class LikeDTO {

    @NotNull(message = "O ID do usuário não pode ser nulo.")
    @Schema(description = "ID do usuário que está curtindo ou descurtindo", example = "1")
    private Long userId;

    @NotNull(message = "O ID do post não pode ser nulo.")
    @Schema(description = "ID do post que está sendo curtido ou descurtido", example = "50")
    private Long postId;
}