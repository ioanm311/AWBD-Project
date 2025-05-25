package com.proiect.awbd.proiect_awbd.service.impl;

import com.proiect.awbd.proiect_awbd.dto.UserDTO;
import com.proiect.awbd.proiect_awbd.dto.UserRequestDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.User;
import com.proiect.awbd.proiect_awbd.repository.UserRepository;
import com.proiect.awbd.proiect_awbd.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO saveUser(UserRequestDTO userRequestDTO) {
        logger.info("Saving new user: {}", userRequestDTO.getUsername());
        if (userRequestDTO.getPassword() == null || userRequestDTO.getPassword().isBlank()) {
            logger.warn("Attempt to save user with empty password");
            throw new IllegalArgumentException("Password must not be empty");
        }
        User user = mapToEntity(userRequestDTO);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        User saved = userRepository.save(user);
        logger.info("User saved with id: {}", saved.getUserId());
        return mapToDTO(saved);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDTO> getAllUsersPaginated(Pageable pageable) {
        logger.info("Fetching users with pagination - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return userRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    @Override
    public UserDTO getUserById(Long id) {
        logger.info("Fetching user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User with id {} not found", id);
                    return new ResourceNotFoundException("User with id " + id + " not found");
                });
        return mapToDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        logger.info("Deleting user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User with id {} not found for deletion", id);
                    return new ResourceNotFoundException("User with id " + id + " not found");
                });

        if (!user.getBookings().isEmpty()) {
            logger.warn("Attempt to delete user with active bookings, id: {}", id);
            throw new IllegalStateException("Cannot delete user with active bookings");
        }

        userRepository.delete(user);
        logger.info("User deleted successfully: {}", id);
    }

    @Override
    public UserDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        logger.info("Updating user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User with id {} not found for update", id);
                    return new ResourceNotFoundException("User not found");
                });

        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setRole(userRequestDTO.getRole());

        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isBlank()) {
            logger.info("Updating password for user id: {}", id);
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }

        User updated = userRepository.save(user);
        logger.info("User updated successfully with id: {}", updated.getUserId());
        return mapToDTO(updated);
    }

    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    private User mapToEntity(UserRequestDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        return user;
    }
}