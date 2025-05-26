package com.redesocial.rede_social_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FollowDTO {

    @NotNull(message = "O ID do seguidor não pode ser nulo.")
    private Long followerId;

    @NotNull(message = "O ID do usuário a ser seguido não pode ser nulo.")
    private Long followedId;

}