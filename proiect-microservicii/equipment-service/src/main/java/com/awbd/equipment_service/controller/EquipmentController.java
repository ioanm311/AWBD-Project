package com.awbd.equipment_service.controller;

import com.awbd.equipment_service.dto.EquipmentDTO;
import com.awbd.equipment_service.service.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public List<EquipmentDTO> getAllEquipment() {
        return equipmentService.getAllEquipments();
    }

    @GetMapping("/{id}")
    public EquipmentDTO getById(@PathVariable Long id) {
        return equipmentService.getEquipmentById(id);
    }

    @PostMapping
    public EquipmentDTO create(@RequestBody @Valid EquipmentDTO dto) {
        return equipmentService.saveEquipment(dto);
    }

    @PutMapping("/{id}")
    public EquipmentDTO update(@PathVariable Long id, @RequestBody @Valid EquipmentDTO dto) {
        return equipmentService.updateEquipment(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
    }

    @GetMapping("/paginated")
    public Page<EquipmentDTO> getPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        return equipmentService.getPaginatedEquipments(page, size, sortField, sortDirection);
    }
}