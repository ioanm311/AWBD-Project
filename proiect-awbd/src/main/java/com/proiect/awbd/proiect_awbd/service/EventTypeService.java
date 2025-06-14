package com.proiect.awbd.proiect_awbd.service;

import com.proiect.awbd.proiect_awbd.dto.EventTypeDTO;
import com.proiect.awbd.proiect_awbd.model.EventType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EventTypeService {
    EventTypeDTO saveEventType(EventTypeDTO eventTypeDTO);
    List<EventTypeDTO> getAllEventTypes();
    EventTypeDTO getEventTypeById(Long id);
    void deleteEventType(Long id);
    EventTypeDTO updateEventType(Long id, EventTypeDTO eventTypeDTO);
    Page<EventTypeDTO> getPaginatedEventTypes(int page, int size, String sortField, String sortDirection);
}
