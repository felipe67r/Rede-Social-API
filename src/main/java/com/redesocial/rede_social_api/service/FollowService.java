package com.redesocial.rede_social_api.service;

import com.redesocial.rede_social_api.dto.FollowDTO;
import com.redesocial.rede_social_api.dto.UserResponseDTO;
import com.redesocial.rede_social_api.model.Follow;
import com.redesocial.rede_social_api.model.User;
import com.redesocial.rede_social_api.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FollowService {

    private static final Logger logger = LoggerFactory.getLogger(FollowService.class);

    private final FollowRepository followRepository;
    private final UserService userService;

    @Autowired
    public FollowService(FollowRepository followRepository, UserService userService) {
        this.followRepository = followRepository;
        this.userService = userService;
        logger.info("FollowService inicializado.");
    }

    @Transactional
    public void followUser(FollowDTO followDTO) {
        logger.info("Usuário {} tentando seguir usuário {}.", followDTO.getFollowerId(), followDTO.getFollowedId());
        User follower = userService.findUserEntityById(followDTO.getFollowerId());
        User followed = userService.findUserEntityById(followDTO.getFollowedId());

        if (follower.getId().equals(followed.getId())) {
            logger.warn("Falha ao seguir: Usuário {} tentou seguir a si mesmo.", followDTO.getFollowerId());
            throw new IllegalArgumentException("Um usuário não pode seguir a si mesmo.");
        }

        if (followRepository.existsByFollowerAndFollowed(follower, followed)) {
            logger.warn("Falha ao seguir: Usuário {} já está seguindo usuário {}.", followDTO.getFollowerId(), followDTO.getFollowedId());
            throw new IllegalArgumentException("Você já está seguindo este usuário.");
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);

        followRepository.save(follow);
        logger.info("Usuário {} agora está seguindo usuário {}.", followDTO.getFollowerId(), followDTO.getFollowedId());
    }

    @Transactional
    public void unfollowUser(FollowDTO followDTO) {
        logger.info("Usuário {} tentando deixar de seguir usuário {}.", followDTO.getFollowerId(), followDTO.getFollowedId());
        User follower = userService.findUserEntityById(followDTO.getFollowerId());
        User followed = userService.findUserEntityById(followDTO.getFollowedId());

        if (!followRepository.existsByFollowerAndFollowed(follower, followed)) {
            logger.warn("Falha ao deixar de seguir: Usuário {} não está seguindo usuário {}.", followDTO.getFollowerId(), followDTO.getFollowedId());
            throw new IllegalArgumentException("Você não está seguindo este usuário.");
        }

        followRepository.deleteByFollowerAndFollowed(follower, followed);
        logger.info("Usuário {} deixou de seguir usuário {}.", followDTO.getFollowerId(), followDTO.getFollowedId());
    }

    public List<UserResponseDTO> getFollowers(Long userId) {
        logger.info("Buscando seguidores para o usuário com ID: {}", userId);
        User user = userService.findUserEntityById(userId);
        List<UserResponseDTO> followers = followRepository.findByFollowed(user).stream()
                .map(follow -> userService.mapUserToUserResponseDTO(follow.getFollower()))
                .collect(Collectors.toList());
        logger.info("Encontrados {} seguidores para o usuário {}.", followers.size(), userId);
        return followers;
    }

    public List<UserResponseDTO> getFollowing(Long userId) {
        logger.info("Buscando quem o usuário {} está seguindo.", userId);
        User user = userService.findUserEntityById(userId);
        List<UserResponseDTO> following = followRepository.findByFollower(user).stream()
                .map(follow -> userService.mapUserToUserResponseDTO(follow.getFollowed()))
                .collect(Collectors.toList());
        logger.info("Usuário {} está seguindo {} usuários.", userId, following.size());
        return following;
    }

    public long countFollowers(Long userId) {
        logger.debug("Contando seguidores para o usuário {}.", userId);
        User user = userService.findUserEntityById(userId);
        long count = followRepository.countByFollowed(user);
        logger.debug("Usuário {} tem {} seguidores.", userId, count);
        return count;
    }

    public long countFollowing(Long userId) {
        logger.debug("Contando quem o usuário {} está seguindo.", userId);
        User user = userService.findUserEntityById(userId);
        long count = followRepository.countByFollower(user);
        logger.debug("Usuário {} está seguindo {} pessoas.", userId, count);
        return count;
    }
}