package com.awbd.equipment_service.service.impl;

import com.awbd.equipment_service.dto.EquipmentDTO;
import com.awbd.equipment_service.exception.ResourceNotFoundException;
import com.awbd.equipment_service.model.Equipment;
import com.awbd.equipment_service.repository.EquipmentRepository;
import com.awbd.equipment_service.service.EquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private static final Logger logger = LoggerFactory.getLogger(EquipmentServiceImpl.class);
    private final EquipmentRepository equipmentRepository;

    public EquipmentServiceImpl(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public EquipmentDTO saveEquipment(EquipmentDTO dto) {
        logger.info("Saving equipment with name: {}", dto.getName());
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
        equipmentRepository.delete(equipment);
    }

    @Override
    public EquipmentDTO updateEquipment(Long id, EquipmentDTO dto) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));

        equipment.setName(dto.getName());
        return mapToDTO(equipmentRepository.save(equipment));
    }

    @Override
    public Page<EquipmentDTO> getPaginatedEquipments(int page, int size, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return equipmentRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    private EquipmentDTO mapToDTO(Equipment equipment) {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setEquipmentId(equipment.getEquipmentId());
        dto.setName(equipment.getName());
        return dto;
    }
}
