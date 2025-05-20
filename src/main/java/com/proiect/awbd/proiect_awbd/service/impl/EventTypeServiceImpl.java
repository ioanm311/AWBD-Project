package com.proiect.awbd.proiect_awbd.service.impl;

import com.proiect.awbd.proiect_awbd.dto.EventTypeDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.EventType;
import com.proiect.awbd.proiect_awbd.repository.EventTypeRepository;
import com.proiect.awbd.proiect_awbd.service.EventTypeService;
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
        logger.debug("Saving EventType: {}", dto.getName());
        EventType eventType = new EventType();
        eventType.setName(dto.getName());
        EventType saved = eventTypeRepository.save(eventType);
        logger.info("EventType saved with id: {}", saved.getEventTypeId());
        return convertToDTO(saved);
    }

    @Override
    public List<EventTypeDTO> getAllEventTypes() {
        logger.debug("Fetching all EventTypes from database");
        return eventTypeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EventTypeDTO getEventTypeById(Long id) {
        logger.debug("Fetching EventType by id: {}", id);
        EventType eventType = eventTypeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("EventType with id {} not found", id);
                    return new ResourceNotFoundException("EventType with id " + id + " not found!");
                });
        return convertToDTO(eventType);
    }

    @Override
    public void deleteEventType(Long id) {
        logger.debug("Attempting to delete EventType with id: {}", id);
        EventType eventType = eventTypeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("EventType with id {} not found for deletion", id);
                    return new ResourceNotFoundException("EventType with id " + id + " not found");
                });

        if (eventType.getBookings() != null && !eventType.getBookings().isEmpty()) {
            logger.warn("Cannot delete EventType {} because it is referenced by bookings", id);
            throw new IllegalStateException("Cannot delete EventType because it is referenced by one or more bookings.");
        }

        eventTypeRepository.delete(eventType);
        logger.info("EventType with id {} successfully deleted", id);
    }

    @Override
    public EventTypeDTO updateEventType(Long id, EventTypeDTO dto) {
        logger.debug("Updating EventType with id: {}", id);
        EventType eventType = eventTypeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("EventType with id {} not found for update", id);
                    return new ResourceNotFoundException("EventType not found");
                });

        eventType.setName(dto.getName());
        EventType updated = eventTypeRepository.save(eventType);
        logger.info("EventType with id {} updated successfully", id);
        return convertToDTO(updated);
    }

    @Override
    public Page<EventTypeDTO> getPaginatedEventTypes(int page, int size, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

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