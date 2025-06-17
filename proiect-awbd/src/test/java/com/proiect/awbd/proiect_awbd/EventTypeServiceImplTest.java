package com.proiect.awbd.proiect_awbd;

import com.proiect.awbd.proiect_awbd.dto.EventTypeDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.Booking;
import com.proiect.awbd.proiect_awbd.model.EventType;
import com.proiect.awbd.proiect_awbd.repository.EventTypeRepository;
import com.proiect.awbd.proiect_awbd.service.impl.EventTypeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventTypeServiceImplTest {

    @Mock  EventTypeRepository eventTypeRepository;
    @InjectMocks
    EventTypeServiceImpl eventTypeService;

    @Test
    void saveEventType_happyPath() {
        EventTypeDTO dto = new EventTypeDTO();
        dto.setName("Workshop");

        when(eventTypeRepository.save(any(EventType.class))).thenAnswer(inv -> {
            EventType et = inv.getArgument(0);
            et.setEventTypeId(3L);
            return et;
        });

        EventTypeDTO saved = eventTypeService.saveEventType(dto);

        assertThat(saved.getEventTypeId()).isEqualTo(3L);
        assertThat(saved.getName()).isEqualTo("Workshop");
    }

    @Test
    void updateEventType_notFound_throws() {
        when(eventTypeRepository.findById(99L)).thenReturn(Optional.empty());

        EventTypeDTO dto = new EventTypeDTO();
        dto.setName("Updated");

        assertThatThrownBy(() -> eventTypeService.updateEventType(99L, dto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteEventType_withBookings_throws() {
        EventType et = new EventType();
        et.setEventTypeId(7L);
        et.setName("Conference");
        et.setBookings(List.of(new Booking()));

        when(eventTypeRepository.findById(7L)).thenReturn(Optional.of(et));

        assertThatThrownBy(() -> eventTypeService.deleteEventType(7L))
                .isInstanceOf(IllegalStateException.class);

        verify(eventTypeRepository, never()).delete(any());
    }

    @Test
    void deleteEventType_success() {
        EventType et = new EventType();
        et.setEventTypeId(8L);
        et.setName("Meet-up");
        et.setBookings(Collections.emptyList());

        when(eventTypeRepository.findById(8L)).thenReturn(Optional.of(et));

        eventTypeService.deleteEventType(8L);

        verify(eventTypeRepository).delete(et);
    }
}
