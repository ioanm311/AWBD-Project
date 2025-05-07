package com.proiect.awbd.proiect_awbd.repository;

import com.proiect.awbd.proiect_awbd.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
