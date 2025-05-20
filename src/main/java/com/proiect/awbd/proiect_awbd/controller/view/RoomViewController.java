package com.proiect.awbd.proiect_awbd.controller.view;

import com.proiect.awbd.proiect_awbd.dto.RoomDTO;
import com.proiect.awbd.proiect_awbd.service.RoomService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rooms")
public class RoomViewController {

    private static final Logger logger = LoggerFactory.getLogger(RoomViewController.class);

    private final RoomService roomService;

    public RoomViewController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public String listRooms(Model model) {
        logger.info("Displaying list of rooms");
        model.addAttribute("rooms", roomService.getAllRooms());
        return "room/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        logger.info("Displaying room creation form");
        model.addAttribute("room", new RoomDTO());
        return "room/create";
    }

    @PostMapping("/create")
    public String createRoom(@ModelAttribute("room") @Valid RoomDTO roomDTO,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()) {
            logger.warn("Validation errors while creating room: {}", result.getAllErrors());
            return "room/create";
        }
        logger.info("Creating room: {}", roomDTO.getName());
        roomService.saveRoom(roomDTO);
        return "redirect:/rooms";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        logger.info("Displaying edit form for room with id: {}", id);
        RoomDTO room = roomService.getRoomById(id);
        model.addAttribute("room", room);
        return "room/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateRoom(@PathVariable Long id,
                             @ModelAttribute("room") @Valid RoomDTO roomDTO,
                             BindingResult result) {
        if (result.hasErrors()) {
            logger.warn("Validation errors while updating room with id {}: {}", id, result.getAllErrors());
            return "room/edit";
        }
        logger.info("Updating room with id: {}", id);
        roomService.updateRoom(id, roomDTO);
        return "redirect:/rooms";
    }

    @GetMapping("/{id}")
    public String viewDetails(@PathVariable Long id, Model model) {
        logger.info("Displaying details for room with id: {}", id);
        RoomDTO room = roomService.getRoomById(id);
        model.addAttribute("room", room);
        return "room/details";
    }

    @PostMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id, Model model) {
        logger.info("Attempting to delete room with id: {}", id);
        try {
            roomService.deleteRoom(id);
        } catch (IllegalStateException e) {
            logger.error("Cannot delete room with id {}: {}", id, e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("rooms", roomService.getAllRooms());
            return "room/list";
        } catch (Exception e) {
            logger.error("Unexpected error while deleting room with id {}: {}", id, e.getMessage());
            return "redirect:/error";
        }
        return "redirect:/rooms";
    }
}