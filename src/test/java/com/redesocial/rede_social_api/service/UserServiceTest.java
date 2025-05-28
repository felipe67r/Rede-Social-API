package com.redesocial.rede_social_api.service;

import com.redesocial.rede_social_api.dto.UserRegisterDTO;
import com.redesocial.rede_social_api.dto.UserResponseDTO;
import com.redesocial.rede_social_api.model.User;
import com.redesocial.rede_social_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRegisterDTO userRegisterDTO;

    @BeforeEach
    void setUp() {
        user = new User(1L, "testuser", "password123", "test@example.com", "Test", "User");
        userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("newuser");
        userRegisterDTO.setEmail("new@example.com");
        userRegisterDTO.setPassword("newpassword");
        userRegisterDTO.setFirstName("New");
        userRegisterDTO.setLastName("User");
    }

    @Test
    void registerUser_Success() {
        when(userRepository.existsByUsername(userRegisterDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userRegisterDTO.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO result = userService.registerUser(userRegisterDTO);

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1)).existsByUsername(userRegisterDTO.getUsername());
        verify(userRepository, times(1)).existsByEmail(userRegisterDTO.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_UsernameAlreadyExists_ThrowsException() {
        when(userRepository.existsByUsername(userRegisterDTO.getUsername())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(userRegisterDTO);
        });

        assertEquals("Nome de usuário já existe.", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername(userRegisterDTO.getUsername());
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_EmailAlreadyExists_ThrowsException() {
        when(userRepository.existsByUsername(userRegisterDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(userRegisterDTO.getEmail())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(userRegisterDTO);
        });

        assertEquals("Email já existe.", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername(userRegisterDTO.getUsername());
        verify(userRepository, times(1)).existsByEmail(userRegisterDTO.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        User user2 = new User(2L, "anotheruser", "pass456", "another@example.com", "Another", "User");
        List<User> users = Arrays.asList(user, user2);
        when(userRepository.findAll()).thenReturn(users);

        List<UserResponseDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(user.getUsername(), result.get(0).getUsername());
        assertEquals(user2.getUsername(), result.get(1).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_UserExists_ReturnsUserResponseDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_UserNotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("Usuário não encontrado com ID: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findUserEntityById_UserExists_ReturnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findUserEntityById(1L);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findUserEntityById_UserNotFound_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.findUserEntityById(1L);
        });

        assertEquals("Usuário não encontrado com ID: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findUserEntityByUsername_UserExists_ReturnsUser() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        User result = userService.findUserEntityByUsername("testuser");

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void findUserEntityByUsername_UserNotFound_ReturnsNull() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        User result = userService.findUserEntityByUsername("nonexistent");

        assertNull(result);
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }

    @Test
    void mapUserToUserResponseDTO_CorrectlyMaps() {
        UserResponseDTO result = userService.mapUserToUserResponseDTO(user);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());
    }
}