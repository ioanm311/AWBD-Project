package com.proiect.awbd.proiect_awbd;

import com.proiect.awbd.proiect_awbd.repository.EventTypeRepository;
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
class EventTypeControllerIT {

    @Autowired MockMvc mockMvc;
    @Autowired EventTypeRepository eventTypeRepository;

    @AfterEach
    void wipe() { eventTypeRepository.deleteAll(); }

    @Test
    void createThenUpdate() throws Exception {
        // POST  /api/event-types
        String create = "{\"name\":\"Seminar\"}";

        String response = mockMvc.perform(post("/api/event-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(create))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String id = response.replaceAll(".*\"eventTypeId\":(\\d+).*", "$1");

        // PUT  /api/event-types/{id}
        String update = "{\"name\":\"Round Table\"}";

        mockMvc.perform(put("/api/event-types/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(update))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Round Table"));
    }
}
