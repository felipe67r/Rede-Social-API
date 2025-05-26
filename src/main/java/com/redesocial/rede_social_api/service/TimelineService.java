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


@Service
public class TimelineService {

    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Autowired
    public TimelineService(PostRepository postRepository, FollowRepository followRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public List<PostResponseDTO> getUserTimeline(Long userId) {

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));


        List<Long> followedUserIds = followRepository.findByFollower(currentUser)
                .stream()
                .map(Follow::getFollowed)
                .map(User::getId)
                .collect(Collectors.toList());


        followedUserIds.add(userId);



        List<Post> timelinePosts = postRepository.findByUserIdIn(followedUserIds);


        return timelinePosts.stream()
                .map(this::convertToPostResponseDTO)
                .sorted(Comparator.comparing(PostResponseDTO::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }


    private PostResponseDTO convertToPostResponseDTO(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getContent(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getCreatedAt()
        );
    }
}