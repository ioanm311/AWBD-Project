package com.proiect.awbd.proiect_awbd.service;

import com.proiect.awbd.proiect_awbd.model.EventType;

import java.util.List;

public interface EventTypeService {
    EventType saveEventType(EventType eventType);
    List<EventType> getAllEventTypes();
    EventType getEventTypeById(Long id);
    void deleteEventType(Long id);
    EventType updateEventType(Long id, EventType eventType);
}
