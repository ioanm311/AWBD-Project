package com.proiect.awbd.proiect_awbd.service;

import com.proiect.awbd.proiect_awbd.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    List<User> getAllUsers();
    User getUserById(Long id);
    void deleteUser(Long id);
    User updateUser(Long id, User user);
}
