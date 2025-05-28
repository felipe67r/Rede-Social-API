package com.redesocial.rede_social_api.service;

import com.redesocial.rede_social_api.dto.UserRegisterDTO;
import com.redesocial.rede_social_api.dto.UserResponseDTO;
import com.redesocial.rede_social_api.model.User;
import com.redesocial.rede_social_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        logger.info("UserService inicializado.");
    }

    @Transactional
    public UserResponseDTO registerUser(UserRegisterDTO registerDTO) {
        logger.info("Tentativa de registro de novo usuário: {}", registerDTO.getUsername());
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            logger.warn("Falha no registro: Nome de usuário já existe: {}", registerDTO.getUsername());
            throw new IllegalArgumentException("Nome de usuário já existe.");
        }
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            logger.warn("Falha no registro: Email já cadastrado: {}", registerDTO.getEmail());
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());
        user.setEmail(registerDTO.getEmail());
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());

        User savedUser = userRepository.save(user);
        logger.info("Usuário registrado com sucesso: {}", savedUser.getUsername());
        return mapUserToUserResponseDTO(savedUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        logger.info("Buscando todos os usuários.");
        List<UserResponseDTO> users = userRepository.findAll().stream()
                .map(this::mapUserToUserResponseDTO)
                .collect(Collectors.toList());
        logger.info("{} usuários encontrados.", users.size());
        return users;
    }

    public UserResponseDTO getUserById(Long id) {
        logger.info("Buscando usuário com ID: {}", id);
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            logger.warn("Usuário não encontrado com ID: {}", id);
            throw new IllegalArgumentException("Usuário não encontrado com ID: " + id);
        }
        logger.info("Usuário com ID {} encontrado.", id);
        return mapUserToUserResponseDTO(userOptional.get());
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserResponseDTO userUpdateDTO) {
        logger.info("Tentativa de atualização do usuário com ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Usuário não encontrado com ID: {}", id);
                    return new IllegalArgumentException("Usuário não encontrado com ID: " + id);
                });

        if (userRepository.existsByEmail(userUpdateDTO.getEmail()) && !user.getEmail().equals(userUpdateDTO.getEmail())) {
            logger.warn("Falha na atualização: Email já cadastrado para outro usuário: {}", userUpdateDTO.getEmail());
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        user.setEmail(userUpdateDTO.getEmail());

        User updatedUser = userRepository.save(user);
        logger.info("Usuário com ID {} atualizado com sucesso.", id);
        return mapUserToUserResponseDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        logger.info("Tentativa de exclusão do usuário com ID: {}", id);
        if (!userRepository.existsById(id)) {
            logger.warn("Falha na exclusão: Usuário não encontrado com ID: {}", id);
            throw new IllegalArgumentException("Usuário não encontrado com ID: " + id);
        }
        userRepository.deleteById(id);
        logger.info("Usuário com ID {} excluído com sucesso.", id);
    }

    public User findUserEntityById(Long id) {
        logger.debug("Buscando entidade de usuário com ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Entidade de usuário não encontrada com ID: {}", id);
                    return new IllegalArgumentException("Usuário não encontrado com ID: " + id);
                });
    }

    public User findUserEntityByUsername(String username) {
        logger.debug("Buscando entidade de usuário com username: {}", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.warn("Entidade de usuário não encontrada com username: {}", username);
        } else {
            logger.debug("Entidade de usuário com username {} encontrada.", username);
        }
        return user;
    }

    public UserResponseDTO mapUserToUserResponseDTO(User user) {
        logger.debug("Mapeando User para UserResponseDTO para usuário: {}", user.getUsername());
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
}