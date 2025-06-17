package com.proiect.awbd.proiect_awbd;

import com.proiect.awbd.proiect_awbd.repository.EquipmentRepository;
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
@AutoConfigureMockMvc(addFilters = false)   // skip Spring-Security filters
@ActiveProfiles("test")
class EquipmentControllerIT {

    @Autowired MockMvc mockMvc;
    @Autowired EquipmentRepository equipmentRepository;

    @AfterEach
    void cleanUp() { equipmentRepository.deleteAll(); }

    @Test
    void createThenList() throws Exception {
        String json = "{\"name\":\"Projector\"}";

        mockMvc.perform(post("/api/equipment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.equipmentId").exists())
                .andExpect(jsonPath("$.name").value("Projector"));

        mockMvc.perform(get("/api/equipment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Projector"));
    }
}
