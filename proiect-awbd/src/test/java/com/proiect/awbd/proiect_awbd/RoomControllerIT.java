package com.proiect.awbd.proiect_awbd;

import com.proiect.awbd.proiect_awbd.repository.RoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class RoomControllerIT {

    @Autowired MockMvc mockMvc;
    @Autowired RoomRepository roomRepository;

    @AfterEach
    void cleanUp() { roomRepository.deleteAll(); }

    @Test
    void createRoom_thenListRooms() throws Exception {
        String json = """
            {
              "name": "Conference-1",
              "capacity": 25
            }
            """;

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomId").exists())
                .andExpect(jsonPath("$.name").value("Conference-1"));

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].capacity").value(25));
    }
}
