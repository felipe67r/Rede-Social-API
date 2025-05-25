package com.redesocial.rede_social_api.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PostResponseDTO {
    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;

}