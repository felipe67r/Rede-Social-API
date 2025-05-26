package com.redesocial.rede_social_api.service;

import com.redesocial.rede_social_api.dto.LikeDTO;
import com.redesocial.rede_social_api.model.Like;
import com.redesocial.rede_social_api.model.Post;
import com.redesocial.rede_social_api.model.User;
import com.redesocial.rede_social_api.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LikeService {

    private static final Logger logger = LoggerFactory.getLogger(LikeService.class);

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public LikeService(LikeRepository likeRepository, UserService userService, PostService postService) {
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.postService = postService;
        logger.info("LikeService inicializado.");
    }

    @Transactional
    public void likePost(LikeDTO likeDTO) {
        logger.info("Usuário {} tentando curtir o post {}", likeDTO.getUserId(), likeDTO.getPostId());
        User user = userService.findUserEntityById(likeDTO.getUserId());
        Post post = postService.findPostEntityById(likeDTO.getPostId());

        if (likeRepository.existsByUserAndPost(user, post)) {
            logger.warn("Falha ao curtir: Usuário {} já curtiu o post {}", likeDTO.getUserId(), likeDTO.getPostId());
            throw new IllegalArgumentException("Você já curtiu este post.");
        }

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);

        likeRepository.save(like);
        logger.info("Usuário {} curtiu o post {} com sucesso.", likeDTO.getUserId(), likeDTO.getPostId());
    }

    @Transactional
    public void unlikePost(LikeDTO likeDTO) {
        logger.info("Usuário {} tentando descurtir o post {}", likeDTO.getUserId(), likeDTO.getPostId());
        User user = userService.findUserEntityById(likeDTO.getUserId());
        Post post = postService.findPostEntityById(likeDTO.getPostId());

        if (!likeRepository.existsByUserAndPost(user, post)) {
            logger.warn("Falha ao descurtir: Usuário {} não curtiu o post {}", likeDTO.getUserId(), likeDTO.getPostId());
            throw new IllegalArgumentException("Você não curtiu este post.");
        }

        likeRepository.deleteByUserAndPost(user, post);
        logger.info("Usuário {} descurtiu o post {} com sucesso.", likeDTO.getUserId(), likeDTO.getPostId());
    }

    public long countLikesForPost(Long postId) {
        logger.debug("Contando curtidas para o post {}", postId);
        Post post = postService.findPostEntityById(postId);
        long count = likeRepository.countByPost(post);
        logger.debug("Post {} tem {} curtidas.", postId, count);
        return count;
    }

    public boolean hasUserLikedPost(Long userId, Long postId) {
        logger.debug("Verificando se o usuário {} curtiu o post {}", userId, postId);
        User user = userService.findUserEntityById(userId);
        Post post = postService.findPostEntityById(postId);
        boolean hasLiked = likeRepository.existsByUserAndPost(user, post);
        logger.debug("Usuário {} {} curtiu o post {}", userId, hasLiked ? "já" : "não", postId);
        return hasLiked;
    }
}