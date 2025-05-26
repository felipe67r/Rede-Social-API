
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

@RestController
@RequestMapping("/api/timeline")
public class TimelineController {

    private final TimelineService timelineService;

    @Autowired
    public TimelineController(TimelineService timelineService) {
        this.timelineService = timelineService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<PostResponseDTO>> getTimeline(@PathVariable Long userId) {
        List<PostResponseDTO> timeline = timelineService.getUserTimeline(userId);
        return ResponseEntity.ok(timeline);
    }
}