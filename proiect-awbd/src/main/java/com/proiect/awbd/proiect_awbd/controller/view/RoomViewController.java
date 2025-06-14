package com.proiect.awbd.proiect_awbd.controller.view;

import com.proiect.awbd.proiect_awbd.dto.RoomDTO;
import com.proiect.awbd.proiect_awbd.service.RoomService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public String listRooms(Model model,
                            @RequestParam(defaultValue = "0") Integer page,
                            @RequestParam(defaultValue = "5") Integer size,
                            @RequestParam(defaultValue = "name") String sortBy,
                            @RequestParam(defaultValue = "asc") String direction) {
        if (size == null) size = 5;
        if (page == null) page = 0;

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RoomDTO> roomPage = roomService.getAllRoomsPaginated(pageable);

        model.addAttribute("rooms", roomPage.getContent());
        model.addAttribute("currentPage", roomPage.getNumber());
        model.addAttribute("totalPages", roomPage.getTotalPages());
        model.addAttribute("totalItems", roomPage.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");
        model.addAttribute("size", size);

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
    public String deleteRoom(@PathVariable Long id,
                             @RequestParam(defaultValue = "0") Integer page,
                             @RequestParam(defaultValue = "5") Integer size,
                             @RequestParam(defaultValue = "name") String sortBy,
                             @RequestParam(defaultValue = "asc") String direction,
                             Model model) {
        logger.info("Attempting to delete room with id: {}", id);
        try {
            roomService.deleteRoom(id);
        } catch (IllegalStateException e) {
            logger.error("Cannot delete room with id {}: {}", id, e.getMessage());
            Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<RoomDTO> roomPage = roomService.getAllRoomsPaginated(pageable);

            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("rooms", roomPage.getContent());
            model.addAttribute("currentPage", roomPage.getNumber());
            model.addAttribute("totalPages", roomPage.getTotalPages());
            model.addAttribute("totalItems", roomPage.getTotalElements());
            model.addAttribute("sortBy", sortBy);
            model.addAttribute("direction", direction);
            model.addAttribute("reverseDirection", direction.equals("asc") ? "desc" : "asc");

            return "room/list";
        } catch (Exception e) {
            logger.error("Unexpected error while deleting room with id {}: {}", id, e.getMessage());
            return "redirect:/error";
        }
        return "redirect:/rooms";
    }
}