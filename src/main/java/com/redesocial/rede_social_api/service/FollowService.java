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

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService; // Para buscar entidades de usuário

    @Autowired
    public FollowService(FollowRepository followRepository, UserService userService) {
        this.followRepository = followRepository;
        this.userService = userService;
    }

    @Transactional
    public void followUser(FollowDTO followDTO) {
        User follower = userService.findUserEntityById(followDTO.getFollowerId());
        User followed = userService.findUserEntityById(followDTO.getFollowedId());

        // Regra de Negócio: Um usuário não pode seguir a si mesmo
        if (follower.getId().equals(followed.getId())) {
            throw new IllegalArgumentException("Um usuário não pode seguir a si mesmo.");
        }

        // Regra de Negócio: Verificar se já não está seguindo
        if (followRepository.existsByFollowerAndFollowed(follower, followed)) {
            throw new IllegalArgumentException("Você já está seguindo este usuário.");
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);

        followRepository.save(follow);
    }

    @Transactional
    public void unfollowUser(FollowDTO followDTO) {
        User follower = userService.findUserEntityById(followDTO.getFollowerId());
        User followed = userService.findUserEntityById(followDTO.getFollowedId());

        // Regra de Negócio: Verificar se está realmente seguindo para poder deixar de seguir
        if (!followRepository.existsByFollowerAndFollowed(follower, followed)) {
            throw new IllegalArgumentException("Você não está seguindo este usuário.");
        }

        followRepository.deleteByFollowerAndFollowed(follower, followed);
    }

    // Métodos para obter seguidores e quem o usuário segue
    public List<UserResponseDTO> getFollowers(Long userId) {
        User user = userService.findUserEntityById(userId);
        return followRepository.findByFollowed(user).stream()
                .map(follow -> userService.mapUserToUserResponseDTO(follow.getFollower()))
                .collect(Collectors.toList());
    }

    public List<UserResponseDTO> getFollowing(Long userId) {
        User user = userService.findUserEntityById(userId);
        return followRepository.findByFollower(user).stream()
                .map(follow -> userService.mapUserToUserResponseDTO(follow.getFollowed()))
                .collect(Collectors.toList());
    }

    public long countFollowers(Long userId) {
        User user = userService.findUserEntityById(userId);
        return followRepository.countByFollowed(user);
    }

    public long countFollowing(Long userId) {
        User user = userService.findUserEntityById(userId);
        return followRepository.countByFollower(user);
    }
}