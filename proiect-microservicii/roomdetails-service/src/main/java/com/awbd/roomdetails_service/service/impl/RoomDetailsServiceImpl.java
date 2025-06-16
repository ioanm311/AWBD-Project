package com.awbd.roomdetails_service.service.impl;

import com.awbd.roomdetails_service.dto.RoomDetailsDTO;
import com.awbd.roomdetails_service.exception.ResourceNotFoundException;
import com.awbd.roomdetails_service.model.RoomDetails;
import com.awbd.roomdetails_service.repository.RoomDetailsRepository;
import com.awbd.roomdetails_service.service.RoomDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomDetailsServiceImpl implements RoomDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(RoomDetailsServiceImpl.class);
    private final RoomDetailsRepository repository;

    public RoomDetailsServiceImpl(RoomDetailsRepository repository) {
        this.repository = repository;
    }

    private RoomDetailsDTO toDTO(RoomDetails entity) {
        RoomDetailsDTO dto = new RoomDetailsDTO();
        dto.setRoomId(entity.getRoomId());
        dto.setDescription(entity.getDescription());
        dto.setEquipmentInfo(entity.getEquipmentInfo());
        return dto;
    }

    private RoomDetails toEntity(RoomDetailsDTO dto) {
        RoomDetails entity = new RoomDetails();
        entity.setRoomId(dto.getRoomId());
        entity.setDescription(dto.getDescription());
        entity.setEquipmentInfo(dto.getEquipmentInfo());
        return entity;
    }

    @Override
    public RoomDetailsDTO saveRoomDetails(RoomDetailsDTO dto) {
        if (repository.existsById(dto.getRoomId())) {
            throw new IllegalArgumentException("RoomDetails already exists for this Room ID");
        }
        RoomDetails entity = repository.save(toEntity(dto));
        return toDTO(entity);
    }

    @Override
    public List<RoomDetailsDTO> getAllRoomDetails() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoomDetailsDTO getRoomDetailsById(Long id) {
        RoomDetails entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RoomDetails with id " + id + " not found"));
        return toDTO(entity);
    }

    @Override
    public void deleteRoomDetails(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("RoomDetails with id " + id + " not found");
        }
        repository.deleteById(id);
    }

    @Override
    public RoomDetailsDTO updateRoomDetails(Long id, RoomDetailsDTO dto) {
        RoomDetails entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RoomDetails not found"));
        entity.setDescription(dto.getDescription());
        entity.setEquipmentInfo(dto.getEquipmentInfo());
        return toDTO(repository.save(entity));
    }

    @Override
    public Page<RoomDetailsDTO> getPaginatedRoomDetails(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDTO);
    }
}
