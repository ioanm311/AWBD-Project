package com.proiect.awbd.proiect_awbd.controller.view;

import com.proiect.awbd.proiect_awbd.dto.EventTypeDTO;
import com.proiect.awbd.proiect_awbd.service.EventTypeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/event-types")
public class EventTypeViewController {

    private static final Logger logger = LoggerFactory.getLogger(EventTypeViewController.class);

    private final EventTypeService eventTypeService;

    public EventTypeViewController(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    @GetMapping
    public String listEventTypes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        Page<EventTypeDTO> pageEventTypes = eventTypeService.getPaginatedEventTypes(page, size, sortField, sortDir);

        model.addAttribute("eventTypes", pageEventTypes.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageEventTypes.getTotalPages());
        model.addAttribute("totalItems", pageEventTypes.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "event-types/list";
    }


    @GetMapping("/add")
    public String showAddForm(Model model) {
        logger.info("Displaying form to add a new EventType");
        model.addAttribute("eventTypeDTO", new EventTypeDTO());
        return "event-types/add";
    }

    @PostMapping("/add")
    public String addEventType(@Valid @ModelAttribute("eventTypeDTO") EventTypeDTO dto,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            logger.warn("Validation errors occurred when adding EventType");
            return "event-types/add";
        }
        logger.info("Adding new EventType with name: {}", dto.getName());
        eventTypeService.saveEventType(dto);
        return "redirect:/event-types";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        logger.info("Displaying form to edit EventType with id: {}", id);
        EventTypeDTO dto = eventTypeService.getEventTypeById(id);
        model.addAttribute("eventTypeDTO", dto);
        return "event-types/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateEventType(@PathVariable Long id,
                                  @Valid @ModelAttribute("eventTypeDTO") EventTypeDTO dto,
                                  BindingResult result) {
        if (result.hasErrors()) {
            logger.warn("Validation errors occurred when updating EventType with id: {}", id);
            return "event-types/edit";
        }
        logger.info("Updating EventType with id: {}", id);
        eventTypeService.updateEventType(id, dto);
        return "redirect:/event-types";
    }

    @GetMapping("/delete/{id}")
    public String deleteEventType(@PathVariable Long id, Model model) {
        logger.info("Attempting to delete EventType with id: {}", id);
        try {
            eventTypeService.deleteEventType(id);
        } catch (Exception e) {
            logger.error("Error deleting EventType with id: {} - {}", id, e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());

            int page = 0;
            int size = 5;
            String sortField = "name";
            String sortDir = "asc";
            Page<EventTypeDTO> pageEventTypes = eventTypeService.getPaginatedEventTypes(page, size, sortField, sortDir);

            model.addAttribute("eventTypes", pageEventTypes.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pageEventTypes.getTotalPages());
            model.addAttribute("totalItems", pageEventTypes.getTotalElements());
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

            return "event-types/list";
        }
        return "redirect:/event-types";
    }

}