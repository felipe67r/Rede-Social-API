package com.redesocial.rede_social_api.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CommentResponseDTO {
    private Long id;
    private String content;
    private String username;
    private Long postId;
    private LocalDateTime createdAt;
}