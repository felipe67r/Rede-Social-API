package com.redesocial.rede_social_api.controller;

import com.redesocial.rede_social_api.dto.FollowDTO;
import com.redesocial.rede_social_api.dto.UserResponseDTO;
import com.redesocial.rede_social_api.service.FollowService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/follows")
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping
    public ResponseEntity<Void> followUser(@Valid @RequestBody FollowDTO followDTO) {
        followService.followUser(followDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> unfollowUser(@Valid @RequestBody FollowDTO followDTO) {
        followService.unfollowUser(followDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<UserResponseDTO>> getFollowers(@PathVariable Long userId) {
        List<UserResponseDTO> followers = followService.getFollowers(userId);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<List<UserResponseDTO>> getFollowing(@PathVariable Long userId) {
        List<UserResponseDTO> following = followService.getFollowing(userId);
        return ResponseEntity.ok(following);
    }
}