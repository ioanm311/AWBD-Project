package com.proiect.awbd.proiect_awbd.controller.view;

import com.proiect.awbd.proiect_awbd.dto.RoomDetailsDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.service.RoomDetailsService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/room-details")
public class RoomDetailsViewController {

    private static final Logger logger = LoggerFactory.getLogger(RoomDetailsViewController.class);
    private final RoomDetailsService roomDetailsService;

    public RoomDetailsViewController(RoomDetailsService roomDetailsService) {
        this.roomDetailsService = roomDetailsService;
    }

    @GetMapping
    public String listRoomDetails(Model model) {
        logger.info("Displaying list of all RoomDetails");
        model.addAttribute("roomDetailsList", roomDetailsService.getAllRoomDetails());
        return "room-details/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        logger.info("Showing form to add new RoomDetails");
        model.addAttribute("roomDetails", new RoomDetailsDTO());
        return "room-details/form";
    }

    @PostMapping("/save")
    public String saveRoomDetails(@ModelAttribute("roomDetails") @Valid RoomDetailsDTO dto,
                                  BindingResult result,
                                  Model model) {
        if (result.hasErrors()) {
            logger.warn("Validation errors while submitting RoomDetails form");
            return "room-details/form";
        }

        try {
            logger.info("Saving new RoomDetails for roomId: {}", dto.getRoomId());
            roomDetailsService.saveRoomDetails(dto);
            return "redirect:/room-details";
        } catch (IllegalArgumentException | ResourceNotFoundException ex) {
            logger.error("Error saving RoomDetails: {}", ex.getMessage());
            model.addAttribute("errorMessage", ex.getMessage());
            return "room-details/form";
        } catch (Exception ex) {
            logger.error("Unexpected error occurred while saving RoomDetails", ex);
            model.addAttribute("errorMessage", "Unexpected error occurred.");
            return "room-details/form";
        }
    }
}
