package com.awbd.user_service.service;

import com.awbd.user_service.dto.UserDTO;
import com.awbd.user_service.dto.UserRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserDTO saveUser(UserRequestDTO dto);
    List<UserDTO> getAllUsers();
    Page<UserDTO> getAllUsersPaginated(Pageable pageable);
    UserDTO getUserById(Long id);
    UserDTO updateUser(Long id, UserRequestDTO dto);
    void deleteUser(Long id);
}