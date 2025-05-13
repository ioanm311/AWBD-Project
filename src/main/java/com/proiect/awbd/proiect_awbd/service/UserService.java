package com.proiect.awbd.proiect_awbd.service;

import com.proiect.awbd.proiect_awbd.dto.UserDTO;
import com.proiect.awbd.proiect_awbd.dto.UserRequestDTO;
import com.proiect.awbd.proiect_awbd.model.User;

import java.util.List;

public interface UserService {
    UserDTO saveUser(UserRequestDTO userRequestDTO);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    void deleteUser(Long id);
    UserDTO updateUser(Long id, UserRequestDTO userRequestDTO);
}
