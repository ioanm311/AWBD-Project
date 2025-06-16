package com.awbd.eventtype_service.service.impl;

import com.awbd.eventtype_service.dto.EventTypeDTO;
import com.awbd.eventtype_service.exception.ResourceNotFoundException;
import com.awbd.eventtype_service.model.EventType;
import com.awbd.eventtype_service.repository.EventTypeRepository;
import com.awbd.eventtype_service.service.EventTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventTypeServiceImpl implements EventTypeService {

    private static final Logger logger = LoggerFactory.getLogger(EventTypeServiceImpl.class);
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
                .orElseThrow(() -> new ResourceNotFoundException("EventType not found"));
        return convertToDTO(eventType);
    }

    @Override
    public void deleteEventType(Long id) {
        EventType eventType = eventTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EventType not found"));
        eventTypeRepository.delete(eventType);
    }

    @Override
    public EventTypeDTO updateEventType(Long id, EventTypeDTO dto) {
        EventType eventType = eventTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EventType not found"));
        eventType.setName(dto.getName());
        return convertToDTO(eventTypeRepository.save(eventType));
    }

    @Override
    public Page<EventTypeDTO> getPaginatedEventTypes(int page, int size, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return eventTypeRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    private EventTypeDTO convertToDTO(EventType eventType) {
        EventTypeDTO dto = new EventTypeDTO();
        dto.setEventTypeId(eventType.getEventTypeId());
        dto.setName(eventType.getName());
        return dto;
    }
}
