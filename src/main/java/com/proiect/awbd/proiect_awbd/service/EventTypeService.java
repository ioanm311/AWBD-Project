package com.proiect.awbd.proiect_awbd.service;

import com.proiect.awbd.proiect_awbd.dto.EventTypeDTO;
import com.proiect.awbd.proiect_awbd.model.EventType;

import java.util.List;

public interface EventTypeService {
    EventTypeDTO saveEventType(EventTypeDTO eventTypeDTO);
    List<EventTypeDTO> getAllEventTypes();
    EventTypeDTO getEventTypeById(Long id);
    void deleteEventType(Long id);
    EventTypeDTO updateEventType(Long id, EventTypeDTO eventTypeDTO);
}
