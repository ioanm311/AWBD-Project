package com.proiect.awbd.proiect_awbd.controller;

import com.proiect.awbd.proiect_awbd.dto.EventTypeDTO;
import com.proiect.awbd.proiect_awbd.service.EventTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event-types")
public class EventTypeController {

    private final EventTypeService eventTypeService;

    public EventTypeController(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    @PostMapping
    public EventTypeDTO saveEventType(@RequestBody EventTypeDTO dto) {
        return eventTypeService.saveEventType(dto);
    }

    @GetMapping
    public List<EventTypeDTO> getAllEventTypes() {
        return eventTypeService.getAllEventTypes();
    }

    @GetMapping("/{id}")
    public EventTypeDTO getEventTypeById(@PathVariable Long id) {
        return eventTypeService.getEventTypeById(id);
    }

    @PutMapping("/{id}")
    public EventTypeDTO updateEventType(@PathVariable Long id, @RequestBody EventTypeDTO dto) {
        return eventTypeService.updateEventType(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteEventType(@PathVariable Long id) {
        eventTypeService.deleteEventType(id);
    }
}

