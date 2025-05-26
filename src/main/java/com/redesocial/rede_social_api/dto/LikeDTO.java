package com.redesocial.rede_social_api.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
@Data
public class LikeDTO {

    @NotNull(message = "O ID do usuário não pode ser nulo.")
    private Long userId;

    @NotNull(message = "O ID do post não pode ser nulo.")
    private Long postId;
}