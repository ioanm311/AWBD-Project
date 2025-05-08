package com.proiect.awbd.proiect_awbd.service.impl;

import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.EventType;
import com.proiect.awbd.proiect_awbd.repository.EventTypeRepository;
import com.proiect.awbd.proiect_awbd.service.EventTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventTypeServiceImpl implements EventTypeService {

    private final EventTypeRepository eventTypeRepository;

    public EventTypeServiceImpl(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository;
    }

    @Override
    public EventType saveEventType(EventType eventType) {
        return eventTypeRepository.save(eventType);
    }

    @Override
    public List<EventType> getAllEventTypes() {
        return eventTypeRepository.findAll();
    }

    @Override
    public EventType getEventTypeById(Long id) {
        return eventTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EventType with id " + id + " not found!"));
    }

    @Override
    public void deleteEventType(Long id) {
        if (!eventTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("EventType with id " + id + " not found");
        }
        eventTypeRepository.deleteById(id);
    }

    @Override
    public EventType updateEventType(Long id, EventType eventTypeDetails) {
        EventType eventType = eventTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EventType not found"));

        eventType.setName(eventTypeDetails.getName());

        return eventTypeRepository.save(eventType);
    }
}
