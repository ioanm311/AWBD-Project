package com.proiect.awbd.proiect_awbd.controller.view;

import com.proiect.awbd.proiect_awbd.dto.EquipmentDTO;
import com.proiect.awbd.proiect_awbd.service.EquipmentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/equipment")
public class EquipmentViewController {

    private static final Logger logger = LoggerFactory.getLogger(EquipmentViewController.class);
    private final EquipmentService equipmentService;

    public EquipmentViewController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public String getPaginatedEquipment(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        logger.info("Displaying paginated equipment list");
        Page<EquipmentDTO> pageEquipments = equipmentService.getPaginatedEquipments(page, size, sortField, sortDirection);

        model.addAttribute("equipments", pageEquipments);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageEquipments.getTotalPages());
        model.addAttribute("totalItems", pageEquipments.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDirection);
        model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");

        return "equipment/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        logger.info("Showing create equipment form");
        model.addAttribute("equipmentDTO", new EquipmentDTO());
        return "equipment/create";
    }

    @PostMapping("/create")
    public String createEquipment(@ModelAttribute("equipmentDTO") @Valid EquipmentDTO dto,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("Validation errors while creating equipment");
            return "equipment/create";
        }
        logger.info("Creating new equipment with name: {}", dto.getName());
        equipmentService.saveEquipment(dto);
        return "redirect:/equipment";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        logger.info("Showing update form for equipment with ID: {}", id);
        EquipmentDTO dto = equipmentService.getEquipmentById(id);
        model.addAttribute("equipmentDTO", dto);
        return "equipment/update";
    }

    @PostMapping("/update/{id}")
    public String updateEquipment(@PathVariable Long id,
                                  @ModelAttribute("equipmentDTO") @Valid EquipmentDTO dto,
                                  BindingResult result) {
        if (result.hasErrors()) {
            logger.warn("Validation errors while updating equipment with ID: {}", id);
            return "equipment/update";
        }
        logger.info("Updating equipment with ID: {}", id);
        equipmentService.updateEquipment(id, dto);
        return "redirect:/equipment";
    }

    @GetMapping("/delete/{id}")
    public String deleteEquipment(@PathVariable Long id, Model model) {
        try {
            logger.info("Deleting equipment with ID: {}", id);
            equipmentService.deleteEquipment(id);
        } catch (IllegalStateException e) {
            logger.error("Error deleting equipment with ID {}: {}", id, e.getMessage());

            // Folosește paginația implicită ca să trimiți un Page, nu List
            int page = 0;
            int size = 5;
            String sortField = "name";
            String sortDir = "asc";

            Page<EquipmentDTO> pageEquipments = equipmentService.getPaginatedEquipments(page, size, sortField, sortDir);

            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("equipments", pageEquipments);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pageEquipments.getTotalPages());
            model.addAttribute("totalItems", pageEquipments.getTotalElements());
            model.addAttribute("sortField", sortField);
            model.addAttribute("sortDir", sortDir);
            model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

            return "equipment/list";
        }
        return "redirect:/equipment";
    }

}
