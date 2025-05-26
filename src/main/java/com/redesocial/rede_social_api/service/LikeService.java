package com.redesocial.rede_social_api.service;

import com.redesocial.rede_social_api.dto.LikeDTO;
import com.redesocial.rede_social_api.model.Like;
import com.redesocial.rede_social_api.model.Post;
import com.redesocial.rede_social_api.model.User;
import com.redesocial.rede_social_api.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public LikeService(LikeRepository likeRepository, UserService userService, PostService postService) {
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.postService = postService;
    }

    @Transactional
    public void likePost(LikeDTO likeDTO) {
        User user = userService.findUserEntityById(likeDTO.getUserId());
        Post post = postService.findPostEntityById(likeDTO.getPostId());

        if (likeRepository.existsByUserAndPost(user, post)) {
            throw new IllegalArgumentException("Você já curtiu este post.");
        }

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);

        likeRepository.save(like);
    }

    @Transactional
    public void unlikePost(LikeDTO likeDTO) {
        User user = userService.findUserEntityById(likeDTO.getUserId());
        Post post = postService.findPostEntityById(likeDTO.getPostId());

        if (!likeRepository.existsByUserAndPost(user, post)) {
            throw new IllegalArgumentException("Você não curtiu este post.");
        }

        likeRepository.deleteByUserAndPost(user, post);
    }

    public long countLikesForPost(Long postId) {

        Post post = postService.findPostEntityById(postId);
        return likeRepository.countByPost(post);
    }

    public boolean hasUserLikedPost(Long userId, Long postId) {
        User user = userService.findUserEntityById(userId);
        Post post = postService.findPostEntityById(postId);
        return likeRepository.existsByUserAndPost(user, post);
    }
}