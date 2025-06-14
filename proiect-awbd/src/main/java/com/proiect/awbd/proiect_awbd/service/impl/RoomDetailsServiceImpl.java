package com.proiect.awbd.proiect_awbd.service.impl;

import com.proiect.awbd.proiect_awbd.dto.RoomDetailsDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.Room;
import com.proiect.awbd.proiect_awbd.model.RoomDetails;
import com.proiect.awbd.proiect_awbd.repository.RoomDetailsRepository;
import com.proiect.awbd.proiect_awbd.repository.RoomRepository;
import com.proiect.awbd.proiect_awbd.service.RoomDetailsService;
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
    private final RoomDetailsRepository roomDetailsRepository;
    private final RoomRepository roomRepository;

    public RoomDetailsServiceImpl(RoomDetailsRepository roomDetailsRepository, RoomRepository roomRepository) {
        this.roomDetailsRepository = roomDetailsRepository;
        this.roomRepository = roomRepository;
    }

    private RoomDetailsDTO mapToDTO(RoomDetails roomDetails) {
        RoomDetailsDTO dto = new RoomDetailsDTO();
        dto.setRoomId(roomDetails.getRoomId());
        dto.setDescription(roomDetails.getDescription());
        dto.setEquipmentInfo(roomDetails.getEquipmentInfo());
        return dto;
    }

    private RoomDetails mapToEntity(RoomDetailsDTO dto) {
        RoomDetails entity = new RoomDetails();
        entity.setRoomId(dto.getRoomId());
        entity.setDescription(dto.getDescription());
        entity.setEquipmentInfo(dto.getEquipmentInfo());

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> {
                    logger.error("Room with id {} not found", dto.getRoomId());
                    return new ResourceNotFoundException("Room with id " + dto.getRoomId() + " not found");
                });
        entity.setRoom(room);
        return entity;
    }

    @Override
    public RoomDetailsDTO saveRoomDetails(RoomDetailsDTO dto) {
        logger.info("Attempting to save RoomDetails for roomId: {}", dto.getRoomId());

        if (roomDetailsRepository.existsById(dto.getRoomId())) {
            logger.warn("RoomDetails already exists for roomId: {}", dto.getRoomId());
            throw new IllegalArgumentException("RoomDetails already exists for this Room ID");
        }

        RoomDetails entity = mapToEntity(dto);
        RoomDetails saved = roomDetailsRepository.save(entity);
        logger.info("RoomDetails saved successfully for roomId: {}", saved.getRoomId());
        return mapToDTO(saved);
    }

    @Override
    public List<RoomDetailsDTO> getAllRoomDetails() {
        logger.info("Fetching all RoomDetails from database");
        return roomDetailsRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<RoomDetailsDTO> getPaginatedRoomDetails(Pageable pageable) {
        return roomDetailsRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    @Override
    public RoomDetailsDTO getRoomDetailsById(Long id) {
        logger.info("Fetching RoomDetails with id: {}", id);
        RoomDetails entity = roomDetailsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("RoomDetails with id {} not found", id);
                    return new ResourceNotFoundException("RoomDetails with id " + id + " not found");
                });
        return mapToDTO(entity);
    }

    @Override
    public void deleteRoomDetails(Long id) {
        logger.info("Attempting to delete RoomDetails with id: {}", id);

        if (!roomDetailsRepository.existsById(id)) {
            logger.warn("RoomDetails with id {} not found, cannot delete", id);
            throw new ResourceNotFoundException("RoomDetails with id " + id + " not found");
        }

        roomDetailsRepository.deleteById(id);
        logger.info("RoomDetails with id {} deleted successfully", id);
    }

    @Override
    public RoomDetailsDTO updateRoomDetails(Long id, RoomDetailsDTO dto) {
        logger.info("Updating RoomDetails with id: {}", id);
        RoomDetails entity = roomDetailsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("RoomDetails with id {} not found", id);
                    return new ResourceNotFoundException("RoomDetails not found");
                });

        entity.setDescription(dto.getDescription());
        entity.setEquipmentInfo(dto.getEquipmentInfo());

        if (!entity.getRoomId().equals(dto.getRoomId())) {
            logger.info("Changing associated room from {} to {}", entity.getRoomId(), dto.getRoomId());
            Room newRoom = roomRepository.findById(dto.getRoomId())
                    .orElseThrow(() -> {
                        logger.error("New Room with id {} not found", dto.getRoomId());
                        return new ResourceNotFoundException("Room with id " + dto.getRoomId() + " not found");
                    });
            entity.setRoom(newRoom);
            entity.setRoomId(dto.getRoomId());
        }

        RoomDetails updated = roomDetailsRepository.save(entity);
        logger.info("RoomDetails with id {} updated successfully", id);
        return mapToDTO(updated);
    }
}