package com.proiect.awbd.proiect_awbd;

import com.proiect.awbd.proiect_awbd.dto.UserDTO;
import com.proiect.awbd.proiect_awbd.dto.UserRequestDTO;
import com.proiect.awbd.proiect_awbd.model.User;
import com.proiect.awbd.proiect_awbd.repository.UserRepository;
import com.proiect.awbd.proiect_awbd.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock  UserRepository userRepository;
    @Mock  PasswordEncoder passwordEncoder;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    void saveUser_happyPath() {
        UserRequestDTO req = new UserRequestDTO();
        req.setUsername("john");
        req.setPassword("secret");
        req.setEmail("john@example.com");
        req.setRole("USER");

        when(passwordEncoder.encode("secret")).thenReturn("ENC");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setUserId(1L);
            return u;
        });

        UserDTO dto = userService.saveUser(req);

        assertThat(dto.getUserId()).isEqualTo(1L);
        assertThat(dto.getUsername()).isEqualTo("john");
        verify(passwordEncoder, times(2)).encode("secret");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void saveUser_emptyPassword_throws() {
        UserRequestDTO req = new UserRequestDTO();
        req.setUsername("john");
        req.setPassword("");     // empty
        req.setEmail("john@example.com");
        req.setRole("USER");

        assertThatThrownBy(() -> userService.saveUser(req))
                .isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(userRepository, passwordEncoder);
    }
}

