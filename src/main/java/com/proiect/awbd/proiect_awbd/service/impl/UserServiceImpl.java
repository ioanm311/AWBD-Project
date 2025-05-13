package com.proiect.awbd.proiect_awbd.service.impl;

import com.proiect.awbd.proiect_awbd.dto.UserDTO;
import com.proiect.awbd.proiect_awbd.dto.UserRequestDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.User;
import com.proiect.awbd.proiect_awbd.repository.UserRepository;
import com.proiect.awbd.proiect_awbd.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO saveUser(UserRequestDTO userRequestDTO) {
        if (userRequestDTO.getPassword() == null || userRequestDTO.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password must not be empty");
        }
        User user = mapToEntity(userRequestDTO);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        User saved = userRepository.save(user);
        return mapToDTO(saved);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        return mapToDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setRole(userRequestDTO.getRole());

        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }

        User updated = userRepository.save(user);
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