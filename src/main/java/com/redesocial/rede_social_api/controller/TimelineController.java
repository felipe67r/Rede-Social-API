package com.redesocial.rede_social_api.controller;

import com.redesocial.rede_social_api.dto.PostResponseDTO;
import com.redesocial.rede_social_api.service.TimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/timeline")
@Tag(name = "Timeline", description = "Operações relacionadas à linha do tempo de posts")
public class TimelineController {

    private final TimelineService timelineService;

    @Autowired
    public TimelineController(TimelineService timelineService) {
        this.timelineService = timelineService;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Obter a timeline de um usuário",
            description = "Retorna uma lista de posts da timeline de um usuário, incluindo posts de quem ele segue e seus próprios posts.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Timeline retornada com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PostResponseDTO[].class))),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"message\": \"Usuário não encontrado com ID: X\"}")))
            })
    public ResponseEntity<List<PostResponseDTO>> getTimeline(
            @Parameter(description = "ID do usuário para buscar a timeline")
            @PathVariable Long userId) {
        List<PostResponseDTO> timeline = timelineService.getUserTimeline(userId);
        return ResponseEntity.ok(timeline);
    }
}