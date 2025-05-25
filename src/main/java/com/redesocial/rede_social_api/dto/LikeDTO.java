package com.redesocial.rede_social_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class LikeDTO {

    @NotNull(message = "ID do post a ser curtido é obrigatório")
    private Long postId;

}
