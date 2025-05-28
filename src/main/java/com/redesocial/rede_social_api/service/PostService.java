package com.redesocial.rede_social_api.service;

import com.redesocial.rede_social_api.dto.PostCreateDTO;
import com.redesocial.rede_social_api.dto.PostResponseDTO;
import com.redesocial.rede_social_api.model.Post;
import com.redesocial.rede_social_api.model.User;
import com.redesocial.rede_social_api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
        logger.info("PostService inicializado.");
    }

    @Transactional
    public PostResponseDTO createPost(PostCreateDTO postCreateDTO, Long userId) {
        logger.info("Usuário {} tentando criar um novo post.", userId);
        User user = userService.findUserEntityById(userId);

        Post post = new Post();
        post.setContent(postCreateDTO.getContent());
        post.setUser(user);

        Post savedPost = postRepository.save(post);
        logger.info("Post criado com sucesso pelo usuário {} (ID do Post: {}).", userId, savedPost.getId());
        return mapPostToPostResponseDTO(savedPost);
    }

    public List<PostResponseDTO> getAllPosts() {
        logger.info("Buscando todos os posts ordenados por data de criação.");
        List<PostResponseDTO> posts = postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapPostToPostResponseDTO)
                .collect(Collectors.toList());
        logger.info("{} posts encontrados no total.", posts.size());
        return posts;
    }

    public PostResponseDTO getPostById(Long postId) {
        logger.info("Buscando post com ID: {}", postId);
        return postRepository.findById(postId)
                .map(this::mapPostToPostResponseDTO)
                .orElseThrow(() -> {
                    logger.warn("Post não encontrado com ID: {}", postId);
                    return new IllegalArgumentException("Post não encontrado com ID: " + postId);
                });
    }

    public Post findPostEntityById(Long postId) {
        logger.debug("Buscando entidade de post com ID: {}", postId);
        return postRepository.findById(postId)
                .orElseThrow(() -> {
                    logger.warn("Entidade de post não encontrada com ID: {}", postId);
                    return new IllegalArgumentException("Post não encontrado com ID: " + postId);
                });
    }

    @Transactional
    public PostResponseDTO updatePost(Long postId, PostCreateDTO updatedPostDTO, Long userId) {
        logger.info("Usuário {} tentando atualizar o post com ID: {}", userId, postId);
        Post existingPost = findPostEntityById(postId);

        if (!existingPost.getUser().getId().equals(userId)) {
            logger.warn("Falha ao atualizar post {}: Usuário {} não tem permissão.", postId, userId);
            throw new SecurityException("Você não tem permissão para editar este post.");
        }

        if (updatedPostDTO.getContent() == null || updatedPostDTO.getContent().trim().isEmpty()) {
            logger.warn("Falha ao atualizar post {}: Conteúdo do post vazio.", postId);
            throw new IllegalArgumentException("O conteúdo do post não pode ser vazio.");
        }
        if (updatedPostDTO.getContent().length() > 280) {
            logger.warn("Falha ao atualizar post {}: Conteúdo muito longo ({} caracteres).", postId, updatedPostDTO.getContent().length());
            throw new IllegalArgumentException("O post deve ter no máximo 280 caracteres.");
        }

        existingPost.setContent(updatedPostDTO.getContent());
        Post updatedPost = postRepository.save(existingPost);
        logger.info("Post {} atualizado com sucesso pelo usuário {}.", postId, userId);
        return mapPostToPostResponseDTO(updatedPost);
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        logger.info("Usuário {} tentando deletar o post com ID: {}", userId, postId);
        Post existingPost = findPostEntityById(postId);

        if (!existingPost.getUser().getId().equals(userId)) {
            logger.warn("Falha ao deletar post {}: Usuário {} não tem permissão.", postId, userId);
            throw new SecurityException("Você não tem permissão para deletar este post.");
        }

        postRepository.delete(existingPost);
        logger.info("Post {} deletado com sucesso pelo usuário {}.", postId, userId);
    }

    public List<PostResponseDTO> getPostsByUserId(Long userId) {
        logger.info("Buscando posts para o usuário com ID: {}", userId);
        userService.findUserEntityById(userId);
        List<PostResponseDTO> posts = postRepository.findByUser_Id(userId).stream()
                .map(this::mapPostToPostResponseDTO)
                .collect(Collectors.toList());
        logger.info("Encontrados {} posts para o usuário {}.", posts.size(), userId);
        return posts;
    }

    private PostResponseDTO mapPostToPostResponseDTO(Post post) {
        logger.debug("Mapeando Post para PostResponseDTO para post ID: {}", post.getId());
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setContent(post.getContent());

        if (post.getUser() != null) {
            logger.debug("User object exists for post ID {}. User ID from Post: {}, Username from Post: {}",
                    post.getId(), post.getUser().getId(), post.getUser().getUsername());
            dto.setUsername(post.getUser().getUsername());
            dto.setUserId(post.getUser().getId());
        } else {
            logger.warn("O objeto User no Post é nulo para o post ID: {}. userId e username serão nulos na resposta.", post.getId());
            dto.setUserId(null);
            dto.setUsername(null);
        }

        dto.setCreatedAt(post.getCreatedAt());
        return dto;
    }
}