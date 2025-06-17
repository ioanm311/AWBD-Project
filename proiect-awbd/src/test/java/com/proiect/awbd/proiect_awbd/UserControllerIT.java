package com.proiect.awbd.proiect_awbd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proiect.awbd.proiect_awbd.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.jpa.properties.hibernate.globally_quoted_identifiers=true")
class UserControllerIT {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper mapper;
    @Autowired UserRepository userRepository;

    @AfterEach
    void wipe() { userRepository.deleteAll(); }

    @Test
    void registerUser_thenListUsers() throws Exception {
        record Req(String username,String password,String email,String role){}
        Req body = new Req("bob","secret123","bob@ex.com","USER");
        String json = mapper.writeValueAsString(body);

        /* ---------- POST /api/users ----- */
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.username").value("bob"));

        /* ---------- GET /api/users ------ */
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("bob@ex.com"));
    }
}
