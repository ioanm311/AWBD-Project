package com.proiect.awbd.proiect_awbd.controller;

import com.proiect.awbd.proiect_awbd.model.EventType;
import com.proiect.awbd.proiect_awbd.service.EventTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*@RestController
@RequestMapping("/api/event-types")
public class EventTypeController {

    private final EventTypeService eventTypeService;

    public EventTypeController(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    @PostMapping
    public EventType createEventType(@RequestBody EventType eventType) {
        return eventTypeService.saveEventType(eventType);
    }

    @GetMapping
    public List<EventType> getAllEventTypes() {
        return eventTypeService.getAllEventTypes();
    }

    @GetMapping("/{id}")
    public EventType getEventTypeById(@PathVariable Long id) {
        return eventTypeService.getEventTypeById(id);
    }

    @PutMapping("/{id}")
    public EventType updateEventType(@PathVariable Long id, @RequestBody EventType eventType) {
        return eventTypeService.updateEventType(id, eventType);
    }

    @DeleteMapping("/{id}")
    public void deleteEventType(@PathVariable Long id) {
        eventTypeService.deleteEventType(id);
    }
}*/

