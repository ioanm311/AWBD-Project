package com.proiect.awbd.proiect_awbd.controller;

import com.proiect.awbd.proiect_awbd.dto.EquipmentDTO;
import com.proiect.awbd.proiect_awbd.service.EquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private static final Logger logger = LoggerFactory.getLogger(EquipmentController.class);
    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping
    public EquipmentDTO saveEquipment(@RequestBody EquipmentDTO dto) {
        logger.info("Saving new equipment with name: {}", dto.getName());
        return equipmentService.saveEquipment(dto);
    }

    @GetMapping
    public List<EquipmentDTO> getAllEquipments() {
        logger.info("Fetching all equipment");
        return equipmentService.getAllEquipments();
    }

    @GetMapping("/{id}")
    public EquipmentDTO getEquipmentById(@PathVariable Long id) {
        logger.info("Fetching equipment by ID: {}", id);
        return equipmentService.getEquipmentById(id);
    }

    @PutMapping("/{id}")
    public EquipmentDTO updateEquipment(@PathVariable Long id, @RequestBody EquipmentDTO dto) {
        logger.info("Updating equipment with ID: {}", id);
        return equipmentService.updateEquipment(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteEquipment(@PathVariable Long id) {
        logger.info("Deleting equipment with ID: {}", id);
        equipmentService.deleteEquipment(id);
    }
}