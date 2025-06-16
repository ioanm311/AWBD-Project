package com.awbd.eventtype_service.controller;

import com.awbd.eventtype_service.dto.EventTypeDTO;
import com.awbd.eventtype_service.service.EventTypeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/event-types")
public class EventTypeRestController {

    private final EventTypeService eventTypeService;

    public EventTypeRestController(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    @GetMapping
    public List<EventTypeDTO> getAllEventTypes() {
        return eventTypeService.getAllEventTypes();
    }

    @GetMapping("/{id}")
    public EventTypeDTO getEventTypeById(@PathVariable Long id) {
        return eventTypeService.getEventTypeById(id);
    }

    @PostMapping
    public EventTypeDTO createEventType(@Valid @RequestBody EventTypeDTO dto) {
        return eventTypeService.saveEventType(dto);
    }

    @PutMapping("/{id}")
    public EventTypeDTO updateEventType(@PathVariable Long id, @Valid @RequestBody EventTypeDTO dto) {
        return eventTypeService.updateEventType(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteEventType(@PathVariable Long id) {
        eventTypeService.deleteEventType(id);
    }

    @GetMapping("/paginated")
    public Page<EventTypeDTO> getPaginatedEventTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        return eventTypeService.getPaginatedEventTypes(page, size, sortField, sortDirection);
    }
}
