package com.proiect.awbd.proiect_awbd.service.impl;

import com.proiect.awbd.proiect_awbd.dto.EventTypeDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.EventType;
import com.proiect.awbd.proiect_awbd.repository.EventTypeRepository;
import com.proiect.awbd.proiect_awbd.service.EventTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventTypeServiceImpl implements EventTypeService {

    private final EventTypeRepository eventTypeRepository;

    public EventTypeServiceImpl(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository;
    }

    @Override
    public EventTypeDTO saveEventType(EventTypeDTO dto) {
        EventType eventType = new EventType();
        eventType.setName(dto.getName());
        return convertToDTO(eventTypeRepository.save(eventType));
    }

    @Override
    public List<EventTypeDTO> getAllEventTypes() {
        return eventTypeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EventTypeDTO getEventTypeById(Long id) {
        EventType eventType = eventTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EventType with id " + id + " not found!"));
        return convertToDTO(eventType);
    }

    @Override
    public void deleteEventType(Long id) {
        if (!eventTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("EventType with id " + id + " not found");
        }
        eventTypeRepository.deleteById(id);
    }

    @Override
    public EventTypeDTO updateEventType(Long id, EventTypeDTO dto) {
        EventType eventType = eventTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EventType not found"));

        eventType.setName(dto.getName());

        return convertToDTO(eventTypeRepository.save(eventType));
    }

    private EventTypeDTO convertToDTO(EventType eventType) {
        EventTypeDTO dto = new EventTypeDTO();
        dto.setEventTypeId(eventType.getEventTypeId());
        dto.setName(eventType.getName());
        return dto;
    }
}
