package com.proiect.awbd.proiect_awbd;

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
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.jpa.properties.hibernate.globally_quoted_identifiers=true")
class FeedbackControllerIT {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper mapper;

    @Autowired UserRepository userRepository;
    @Autowired RoomRepository roomRepository;
    @Autowired EventTypeRepository eventTypeRepository;
    @Autowired BookingRepository bookingRepository;
    @Autowired FeedbackRepository feedbackRepository;

    @AfterEach
    void clean() { feedbackRepository.deleteAll(); }

    @Test
    void createFeedback_thenRetrieve() throws Exception {
        /* ---------- entities ------------ */
        User user = new User();
        user.setUsername("alice");
        user.setPassword("secret123");
        user.setEmail("a@ex.com");
        user.setRole("USER");
        user = userRepository.save(user);

        Room room = new Room();
        room.setName("Conf-2");
        room.setCapacity(20);
        room = roomRepository.save(room);

        EventType et = new EventType();
        et.setName("Workshop");
        et = eventTypeRepository.save(et);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setEventType(et);
        booking.setDate(LocalDate.now());
        booking.setStartTime(LocalTime.parse("09:00"));
        booking.setEndTime(LocalTime.parse("11:00"));
        booking = bookingRepository.save(booking);

        /* ---------- feedback payload ----- */
        Map<String,Object> fb = new HashMap<>();
        fb.put("bookingId", booking.getBookingId());
        fb.put("comment",   "Great projector!");
        fb.put("rating",    5);

        String json = mapper.writeValueAsString(fb);

        /* ---------- POST /api/feedback --- */
        mockMvc.perform(post("/api/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(booking.getBookingId()))
                .andExpect(jsonPath("$.rating").value(5));

        /* ---------- GET /api/feedback/{id} */
        String getResp = mockMvc.perform(get("/api/feedback/{id}", booking.getBookingId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Great projector!"))
                .andReturn().getResponse().getContentAsString();

        assertThat(getResp).contains("Great projector!");
    }
}
