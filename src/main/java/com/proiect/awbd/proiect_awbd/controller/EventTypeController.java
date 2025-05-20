package com.proiect.awbd.proiect_awbd.controller;

import com.proiect.awbd.proiect_awbd.dto.EventTypeDTO;
import com.proiect.awbd.proiect_awbd.service.EventTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event-types")
public class EventTypeController {

    private static final Logger logger = LoggerFactory.getLogger(EventTypeController.class);

    private final EventTypeService eventTypeService;

    public EventTypeController(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    @PostMapping
    public EventTypeDTO saveEventType(@RequestBody EventTypeDTO dto) {
        logger.info("Saving new EventType with name: {}", dto.getName());
        return eventTypeService.saveEventType(dto);
    }

    @GetMapping
    public List<EventTypeDTO> getAllEventTypes() {
        logger.info("Retrieving all EventTypes");
        return eventTypeService.getAllEventTypes();
    }

    @GetMapping("/{id}")
    public EventTypeDTO getEventTypeById(@PathVariable Long id) {
        logger.info("Retrieving EventType by id: {}", id);
        return eventTypeService.getEventTypeById(id);
    }

    @PutMapping("/{id}")
    public EventTypeDTO updateEventType(@PathVariable Long id, @RequestBody EventTypeDTO dto) {
        logger.info("Updating EventType with id: {}", id);
        return eventTypeService.updateEventType(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteEventType(@PathVariable Long id) {
        logger.info("Deleting EventType with id: {}", id);
        eventTypeService.deleteEventType(id);
    }
}