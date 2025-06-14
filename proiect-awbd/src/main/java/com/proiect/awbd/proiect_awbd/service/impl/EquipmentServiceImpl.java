package com.proiect.awbd.proiect_awbd.service.impl;

import com.proiect.awbd.proiect_awbd.dto.EquipmentDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.Equipment;
import com.proiect.awbd.proiect_awbd.repository.EquipmentRepository;
import com.proiect.awbd.proiect_awbd.service.EquipmentService;
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
        logger.info("Equipment saved with ID: {}", saved.getEquipmentId());
        return mapToDTO(saved);
    }

    @Override
    public List<EquipmentDTO> getAllEquipments() {
        logger.info("Fetching all equipment");
        return equipmentRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EquipmentDTO getEquipmentById(Long id) {
        logger.info("Fetching equipment by ID: {}", id);
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Equipment with ID {} not found", id);
                    return new ResourceNotFoundException("Equipment with id " + id + " not found!");
                });
        return mapToDTO(equipment);
    }

    @Override
    public void deleteEquipment(Long id) {
        logger.info("Deleting equipment with ID: {}", id);
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Equipment with ID {} not found for deletion", id);
                    return new ResourceNotFoundException("Equipment with id " + id + " not found");
                });

        if (!equipment.getBookings().isEmpty()) {
            logger.warn("Cannot delete equipment ID {} because it's associated with bookings", id);
            throw new IllegalStateException("The equipment is associated with a booking.");
        }

        equipmentRepository.delete(equipment);
        logger.info("Equipment with ID {} deleted successfully", id);
    }

    @Override
    public EquipmentDTO updateEquipment(Long id, EquipmentDTO dto) {
        logger.info("Updating equipment with ID: {}", id);
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Equipment with ID {} not found for update", id);
                    return new ResourceNotFoundException("Equipment not found");
                });

        equipment.setName(dto.getName());

        Equipment updated = equipmentRepository.save(equipment);
        logger.info("Equipment with ID {} updated successfully", id);
        return mapToDTO(updated);
    }

    private EquipmentDTO mapToDTO(Equipment equipment) {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setEquipmentId(equipment.getEquipmentId());
        dto.setName(equipment.getName());
        return dto;
    }

    @Override
    public Page<EquipmentDTO> getPaginatedEquipments(int page, int size, String sortField, String sortDirection) {
        logger.info("Fetching paginated equipment: page={}, size={}, sortField={}, sortDirection={}", page, size, sortField, sortDirection);

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return equipmentRepository.findAll(pageable)
                .map(this::mapToDTO);
    }
}
