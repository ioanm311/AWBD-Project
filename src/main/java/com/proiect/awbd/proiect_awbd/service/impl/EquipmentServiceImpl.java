package com.proiect.awbd.proiect_awbd.service.impl;

import com.proiect.awbd.proiect_awbd.dto.EquipmentDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.Equipment;
import com.proiect.awbd.proiect_awbd.repository.EquipmentRepository;
import com.proiect.awbd.proiect_awbd.service.EquipmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public EquipmentServiceImpl(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public EquipmentDTO saveEquipment(EquipmentDTO dto) {
        Equipment equipment = new Equipment();
        equipment.setName(dto.getName());

        Equipment saved = equipmentRepository.save(equipment);
        return mapToDTO(saved);
    }

    @Override
    public List<EquipmentDTO> getAllEquipments() {
        return equipmentRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EquipmentDTO getEquipmentById(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with id " + id + " not found!"));
        return mapToDTO(equipment);
    }

    @Override
    public void deleteEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment with id " + id + " not found"));

        if (!equipment.getBookings().isEmpty()) {
            throw new IllegalStateException("The equipment is associated with a booking.");
        }

        equipmentRepository.delete(equipment);
    }

    @Override
    public EquipmentDTO updateEquipment(Long id, EquipmentDTO dto) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));

        equipment.setName(dto.getName());

        Equipment updated = equipmentRepository.save(equipment);
        return mapToDTO(updated);
    }

    private EquipmentDTO mapToDTO(Equipment equipment) {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setEquipmentId(equipment.getEquipmentId());
        dto.setName(equipment.getName());
        return dto;
    }
}
