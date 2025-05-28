package com.redesocial.rede_social_api.service;

import com.redesocial.rede_social_api.dto.PostCreateDTO;
import com.redesocial.rede_social_api.dto.PostResponseDTO;
import com.redesocial.rede_social_api.model.Post;
import com.redesocial.rede_social_api.model.User;
import com.redesocial.rede_social_api.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostService postService;

    private User testUser;
    private Post testPost;
    private PostCreateDTO postCreateDTO;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "testuser", "password123", "test@example.com", "Test", "User");
        testPost = new Post(101L, "Test content", LocalDateTime.now(), testUser);
        postCreateDTO = new PostCreateDTO();
        postCreateDTO.setContent("New post content");
    }

    @Test
    void createPost_Success() {
        when(userService.findUserEntityById(testUser.getId())).thenReturn(testUser);
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        PostResponseDTO result = postService.createPost(postCreateDTO, testUser.getId());

        assertNotNull(result);
        assertEquals(testPost.getContent(), result.getContent());
        assertEquals(testPost.getUser().getUsername(), result.getUsername());
        verify(userService, times(1)).findUserEntityById(testUser.getId());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void createPost_UserNotFound_ThrowsException() {
        when(userService.findUserEntityById(testUser.getId()))
                .thenThrow(new IllegalArgumentException("Usuário não encontrado com ID: " + testUser.getId()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.createPost(postCreateDTO, testUser.getId());
        });

        assertEquals("Usuário não encontrado com ID: " + testUser.getId(), exception.getMessage());
        verify(userService, times(1)).findUserEntityById(testUser.getId());
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void getAllPosts_ReturnsListOfPosts() {
        Post post2 = new Post(102L, "Another post", LocalDateTime.now(), testUser);
        List<Post> posts = Arrays.asList(testPost, post2);
        when(postRepository.findAll()).thenReturn(posts);

        List<PostResponseDTO> result = postService.getAllPosts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testPost.getContent(), result.get(0).getContent());
        assertEquals(post2.getContent(), result.get(1).getContent());
        verify(postRepository, times(1)).findAll();
    }

    @Test
    void getPostById_PostExists_ReturnsPostResponseDTO() {
        when(postRepository.findById(testPost.getId())).thenReturn(Optional.of(testPost));

        PostResponseDTO result = postService.getPostById(testPost.getId());

        assertNotNull(result);
        assertEquals(testPost.getId(), result.getId());
        assertEquals(testPost.getContent(), result.getContent());
        verify(postRepository, times(1)).findById(testPost.getId());
    }

    @Test
    void getPostById_PostNotFound_ThrowsException() {
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.getPostById(999L);
        });

        assertEquals("Post não encontrado com ID: 999", exception.getMessage());
        verify(postRepository, times(1)).findById(999L);
    }

    @Test
    void findPostEntityById_PostExists_ReturnsPost() {
        when(postRepository.findById(testPost.getId())).thenReturn(Optional.of(testPost));

        Post result = postService.findPostEntityById(testPost.getId());

        assertNotNull(result);
        assertEquals(testPost.getId(), result.getId());
        verify(postRepository, times(1)).findById(testPost.getId());
    }

    @Test
    void findPostEntityById_PostNotFound_ThrowsException() {
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.findPostEntityById(999L);
        });

        assertEquals("Post não encontrado com ID: 999", exception.getMessage());
        verify(postRepository, times(1)).findById(999L);
    }

    @Test
    void updatePost_Success() {
        PostCreateDTO updateDTO = new PostCreateDTO();
        updateDTO.setContent("Updated content");

        User anotherUser = new User(2L, "anotheruser", "pass", "another@example.com", "Another", "User");
        Post postToUpdate = new Post(101L, "Original content", LocalDateTime.now(), anotherUser);

        when(postRepository.findById(postToUpdate.getId())).thenReturn(Optional.of(postToUpdate));
        when(postRepository.save(any(Post.class))).thenReturn(postToUpdate);

        PostResponseDTO result = postService.updatePost(postToUpdate.getId(), updateDTO, anotherUser.getId());

        assertNotNull(result);
        assertEquals(updateDTO.getContent(), result.getContent());
        verify(postRepository, times(1)).findById(postToUpdate.getId());
        verify(postRepository, times(1)).save(postToUpdate);
    }

    @Test
    void updatePost_PostNotFound_ThrowsException() {
        PostCreateDTO updateDTO = new PostCreateDTO();
        updateDTO.setContent("Updated content");

        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.updatePost(999L, updateDTO, testUser.getId());
        });

        assertEquals("Post não encontrado com ID: 999", exception.getMessage());
        verify(postRepository, times(1)).findById(999L);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void updatePost_UserNotAuthorized_ThrowsException() {
        PostCreateDTO updateDTO = new PostCreateDTO();
        updateDTO.setContent("Updated content");

        User unauthorizedUser = new User(2L, "unauth", "pass", "unauth@example.com", "Unauth", "User");
        Post postOwnedByOtherUser = new Post(101L, "Original content", LocalDateTime.now(), testUser);

        when(postRepository.findById(postOwnedByOtherUser.getId())).thenReturn(Optional.of(postOwnedByOtherUser));

        Exception exception = assertThrows(SecurityException.class, () -> {
            postService.updatePost(postOwnedByOtherUser.getId(), updateDTO, unauthorizedUser.getId());
        });

        assertEquals("Você não tem permissão para atualizar este post.", exception.getMessage());
        verify(postRepository, times(1)).findById(postOwnedByOtherUser.getId());
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void deletePost_Success() {
        when(postRepository.findById(testPost.getId())).thenReturn(Optional.of(testPost));
        doNothing().when(postRepository).delete(testPost);

        assertDoesNotThrow(() -> postService.deletePost(testPost.getId(), testUser.getId()));

        verify(postRepository, times(1)).findById(testPost.getId());
        verify(postRepository, times(1)).delete(testPost);
    }

    @Test
    void deletePost_PostNotFound_ThrowsException() {
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.deletePost(999L, testUser.getId());
        });

        assertEquals("Post não encontrado com ID: 999", exception.getMessage());
        verify(postRepository, times(1)).findById(999L);
        verify(postRepository, never()).delete(any(Post.class));
    }

    @Test
    void deletePost_UserNotAuthorized_ThrowsException() {
        User unauthorizedUser = new User(2L, "unauth", "pass", "unauth@example.com", "Unauth", "User");
        Post postOwnedByOtherUser = new Post(101L, "Original content", LocalDateTime.now(), testUser);

        when(postRepository.findById(postOwnedByOtherUser.getId())).thenReturn(Optional.of(postOwnedByOtherUser));

        Exception exception = assertThrows(SecurityException.class, () -> {
            postService.deletePost(postOwnedByOtherUser.getId(), unauthorizedUser.getId());
        });

        assertEquals("Você não tem permissão para deletar este post.", exception.getMessage());
        verify(postRepository, times(1)).findById(postOwnedByOtherUser.getId());
        verify(postRepository, never()).delete(any(Post.class));
    }

    @Test
    void getPostsByUserId_ReturnsListOfPosts() {
        Post post2 = new Post(102L, "Another post by user", LocalDateTime.now(), testUser);
        List<Post> postsByUser = Arrays.asList(testPost, post2);

        when(userService.findUserEntityById(testUser.getId())).thenReturn(testUser);
        when(postRepository.findByUser_Id(testUser.getId())).thenReturn(postsByUser);

        List<PostResponseDTO> result = postService.getPostsByUserId(testUser.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testUser.getUsername(), result.get(0).getUsername());
        verify(userService, times(1)).findUserEntityById(testUser.getId());
        verify(postRepository, times(1)).findByUser_Id(testUser.getId());
    }
}