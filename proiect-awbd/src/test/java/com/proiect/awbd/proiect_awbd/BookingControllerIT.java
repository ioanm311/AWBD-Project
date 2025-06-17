package com.proiect.awbd.proiect_awbd;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proiect.awbd.proiect_awbd.model.*;
import com.proiect.awbd.proiect_awbd.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.jpa.properties.hibernate.globally_quoted_identifiers=true")
class BookingControllerIT {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper mapper;

    @Autowired UserRepository userRepository;
    @Autowired RoomRepository roomRepository;
    @Autowired EventTypeRepository eventTypeRepository;
    @Autowired EquipmentRepository equipmentRepository;
    @Autowired BookingRepository bookingRepository;

    @AfterEach
    void clean() { bookingRepository.deleteAll(); }

    @Test
    void createBooking_thenListBookings() throws Exception {
        /* ---------- entities ------------ */
        User user = new User();
        user.setUsername("john");
        user.setPassword("secret123");
        user.setEmail("john@ex.com");
        user.setRole("USER");
        user = userRepository.save(user);

        Room room = new Room();
        room.setName("Conf-1");
        room.setCapacity(10);
        room = roomRepository.save(room);

        EventType et = new EventType();
        et.setName("Meeting");
        et = eventTypeRepository.save(et);

        Equipment eq = new Equipment();
        eq.setName("Projector");
        eq = equipmentRepository.save(eq);

        /* ---------- request body -------- */
        Map<String,Object> body = new HashMap<>();
        body.put("userId",       user.getUserId());
        body.put("roomId",       room.getRoomId());
        body.put("eventTypeId",  et.getEventTypeId());
        body.put("date",         LocalDate.now().toString());
        body.put("startTime",    "09:00");
        body.put("endTime",      "10:00");
        body.put("equipmentIds", List.of(eq.getEquipmentId()));

        String json = mapper.writeValueAsString(body);

        /* ---------- POST /api/bookings -- */
        String postResp = mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").exists())
                .andReturn().getResponse().getContentAsString();

        long createdId = mapper.readTree(postResp).get("bookingId").asLong();

        /* ---------- GET  /api/bookings -- */
        String listResp = mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode arr = mapper.readTree(listResp);
        JsonNode myBooking = null;
        for (JsonNode n : arr) {
            if (n.get("bookingId").asLong() == createdId) {
                myBooking = n;
                break;
            }
        }
        assertThat(myBooking).as("booking with id just created").isNotNull();
        assertThat(myBooking.get("userId").asLong()).isEqualTo(user.getUserId());
        assertThat(myBooking.get("roomId").asLong()).isEqualTo(room.getRoomId());
    }
}
