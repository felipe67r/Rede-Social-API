package com.redesocial.rede_social_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FollowDTO {

    @NotNull(message = "ID do usuário a ser seguido é obrigatório")
    private Long followedId;

}