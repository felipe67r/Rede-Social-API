package com.redesocial.rede_social_api.service;

import com.redesocial.rede_social_api.dto.PostCreateDTO;
import com.redesocial.rede_social_api.dto.PostResponseDTO;
import com.redesocial.rede_social_api.model.Post;
import com.redesocial.rede_social_api.model.User;
import com.redesocial.rede_social_api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime; // Certifique-se de que este import está presente
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Transactional
    public PostResponseDTO createPost(PostCreateDTO postCreateDTO, Long userId) {
        User user = userService.findUserEntityById(userId);

        Post post = new Post();
        post.setContent(postCreateDTO.getContent());
        post.setUser(user);
        // Não é necessário setar createdAt explicitamente aqui, @CreationTimestamp na entidade Post já faz isso

        Post savedPost = postRepository.save(post);
        return mapPostToPostResponseDTO(savedPost);
    }

    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapPostToPostResponseDTO)
                .collect(Collectors.toList());
    }

    // Este método retorna o DTO para o controlador
    public PostResponseDTO getPostById(Long postId) {
        return postRepository.findById(postId)
                .map(this::mapPostToPostResponseDTO)
                .orElseThrow(() -> new IllegalArgumentException("Post não encontrado com ID: " + postId));
    }

    // NOVO MÉTODO: Retorna a entidade Post para ser usada por outros serviços
    public Post findPostEntityById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post não encontrado com ID: " + postId));
    }

    @Transactional
    public PostResponseDTO updatePost(Long postId, PostCreateDTO updatedPostDTO, Long userId) {
        // Usamos findPostEntityById para obter a entidade Post
        Post existingPost = findPostEntityById(postId);

        if (!existingPost.getUser().getId().equals(userId)) {
            throw new SecurityException("Você não tem permissão para editar este post.");
        }

        if (updatedPostDTO.getContent() == null || updatedPostDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("O conteúdo do post não pode ser vazio.");
        }
        if (updatedPostDTO.getContent().length() > 280) {
            throw new IllegalArgumentException("O post deve ter no máximo 280 caracteres.");
        }

        existingPost.setContent(updatedPostDTO.getContent());
        Post updatedPost = postRepository.save(existingPost);
        return mapPostToPostResponseDTO(updatedPost);
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        // Usamos findPostEntityById para obter a entidade Post
        Post existingPost = findPostEntityById(postId);

        if (!existingPost.getUser().getId().equals(userId)) {
            throw new SecurityException("Você não tem permissão para deletar este post.");
        }

        postRepository.delete(existingPost);
    }

    public List<PostResponseDTO> getPostsByUserId(Long userId) {
        userService.findUserEntityById(userId); // Garante que o usuário exista
        return postRepository.findByUser_Id(userId).stream()
                .map(this::mapPostToPostResponseDTO)
                .collect(Collectors.toList());
    }

    private PostResponseDTO mapPostToPostResponseDTO(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setUsername(post.getUser().getUsername());
        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }
}