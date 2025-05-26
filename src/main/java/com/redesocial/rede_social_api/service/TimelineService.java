package com.redesocial.rede_social_api.service;

import com.redesocial.rede_social_api.dto.PostResponseDTO;
import com.redesocial.rede_social_api.repository.FollowRepository;
import com.redesocial.rede_social_api.repository.PostRepository;
import com.redesocial.rede_social_api.repository.UserRepository;
import com.redesocial.rede_social_api.model.User;
import com.redesocial.rede_social_api.model.Post;
import com.redesocial.rede_social_api.model.Follow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TimelineService {

    private static final Logger logger = LoggerFactory.getLogger(TimelineService.class);

    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Autowired
    public TimelineService(PostRepository postRepository, FollowRepository followRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        logger.info("TimelineService inicializado.");
    }

    public List<PostResponseDTO> getUserTimeline(Long userId) {
        logger.info("Buscando timeline para o usuário com ID: {}", userId);
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("Falha ao buscar timeline: Usuário não encontrado com ID: {}", userId);
                    return new RuntimeException("User not found with ID: " + userId);
                });

        List<Long> followedUserIds = followRepository.findByFollower(currentUser)
                .stream()
                .map(Follow::getFollowed)
                .map(User::getId)
                .collect(Collectors.toList());
        logger.debug("Usuário {} segue {} usuários.", userId, followedUserIds.size());

        followedUserIds.add(userId);
        logger.debug("Incluindo posts do próprio usuário {} na timeline. Total de IDs para buscar: {}", userId, followedUserIds.size());

        List<Post> timelinePosts = postRepository.findByUserIdIn(followedUserIds);
        logger.info("Encontrados {} posts para a timeline do usuário {}.", timelinePosts.size(), userId);

        return timelinePosts.stream()
                .map(this::convertToPostResponseDTO)
                .sorted(Comparator.comparing(PostResponseDTO::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    private PostResponseDTO convertToPostResponseDTO(Post post) {
        logger.debug("Mapeando Post para PostResponseDTO para post ID: {}", post.getId());
        return new PostResponseDTO(
                post.getId(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getCreatedAt()
        );
    }
}