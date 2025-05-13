package com.proiect.awbd.proiect_awbd.controller;

import com.proiect.awbd.proiect_awbd.dto.EquipmentDTO;
import com.proiect.awbd.proiect_awbd.service.EquipmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping
    public EquipmentDTO saveEquipment(@RequestBody EquipmentDTO dto) {
        return equipmentService.saveEquipment(dto);
    }

    @GetMapping
    public List<EquipmentDTO> getAllEquipments() {
        return equipmentService.getAllEquipments();
    }

    @GetMapping("/{id}")
    public EquipmentDTO getEquipmentById(@PathVariable Long id) {
        return equipmentService.getEquipmentById(id);
    }

    @PutMapping("/{id}")
    public EquipmentDTO updateEquipment(@PathVariable Long id, @RequestBody EquipmentDTO dto) {
        return equipmentService.updateEquipment(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
    }
}

