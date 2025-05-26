package com.redesocial.rede_social_api.controller;

import com.redesocial.rede_social_api.dto.LikeDTO;
import com.redesocial.rede_social_api.service.LikeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public ResponseEntity<Void> likePost(@Valid @RequestBody LikeDTO likeDTO) {
        likeService.likePost(likeDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> unlikePost(@Valid @RequestBody LikeDTO likeDTO) {
        likeService.unlikePost(likeDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}