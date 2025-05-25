package com.proiect.awbd.proiect_awbd.service.impl;

import com.proiect.awbd.proiect_awbd.dto.RoomDTO;
import com.proiect.awbd.proiect_awbd.dto.RoomDetailsDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.Room;
import com.proiect.awbd.proiect_awbd.model.RoomDetails;
import com.proiect.awbd.proiect_awbd.repository.RoomRepository;
import com.proiect.awbd.proiect_awbd.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public RoomDTO saveRoom(RoomDTO dto) {
        logger.info("Saving room: {}", dto.getName());
        Room room = mapToEntity(dto);
        Room saved = roomRepository.save(room);
        logger.info("Room saved with id: {}", saved.getRoomId());
        return mapToDTO(saved);
    }

    @Override
    public List<RoomDTO> getAllRooms() {
        logger.info("Fetching all rooms");
        return roomRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<RoomDTO> getAllRoomsPaginated(Pageable pageable) {
        logger.info("Fetching paginated list of rooms");
        return roomRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    @Override
    public RoomDTO getRoomById(Long id) {
        logger.info("Fetching room by id: {}", id);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Room with id {} not found", id);
                    return new ResourceNotFoundException("Room with id " + id + " not found!");
                });
        return mapToDTO(room);
    }

    @Override
    public void deleteRoom(Long id) {
        logger.info("Deleting room with id: {}", id);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Room with id {} not found", id);
                    return new ResourceNotFoundException("Room with id " + id + " not found");
                });

        if (room.getBookings() != null && !room.getBookings().isEmpty()) {
            logger.error("Room with id {} has existing bookings, cannot delete", id);
            throw new IllegalStateException("Room cannot be deleted because it has existing bookings.");
        }

        roomRepository.delete(room);
        logger.info("Room with id {} deleted successfully", id);
    }

    @Override
    public RoomDTO updateRoom(Long id, RoomDTO dto) {
        logger.info("Updating room with id: {}", id);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Room with id {} not found for update", id);
                    return new ResourceNotFoundException("Room not found");
                });

        room.setName(dto.getName());
        room.setCapacity(dto.getCapacity());

        if (dto.getRoomDetails() != null) {
            if (room.getRoomDetails() != null) {
                room.getRoomDetails().setDescription(dto.getRoomDetails().getDescription());
                room.getRoomDetails().setEquipmentInfo(dto.getRoomDetails().getEquipmentInfo());
                logger.info("Updated existing RoomDetails for room id: {}", id);
            } else {
                RoomDetails details = new RoomDetails();
                details.setDescription(dto.getRoomDetails().getDescription());
                details.setEquipmentInfo(dto.getRoomDetails().getEquipmentInfo());
                details.setRoom(room); // bidirectional
                room.setRoomDetails(details);
                logger.info("Created new RoomDetails for room id: {}", id);
            }
        }

        Room updated = roomRepository.save(room);
        logger.info("Room with id {} updated successfully", updated.getRoomId());
        return mapToDTO(updated);
    }

    private RoomDTO mapToDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setRoomId(room.getRoomId());
        dto.setName(room.getName());
        dto.setCapacity(room.getCapacity());

        if (room.getRoomDetails() != null) {
            RoomDetailsDTO detailsDTO = new RoomDetailsDTO();
            detailsDTO.setRoomId(room.getRoomDetails().getRoomId());
            detailsDTO.setDescription(room.getRoomDetails().getDescription());
            dto.setRoomDetails(detailsDTO);
        }

        return dto;
    }

    private Room mapToEntity(RoomDTO dto) {
        Room room = new Room();
        room.setRoomId(dto.getRoomId());
        room.setName(dto.getName());
        room.setCapacity(dto.getCapacity());

        if (dto.getRoomDetails() != null) {
            RoomDetails details = new RoomDetails();
            details.setDescription(dto.getRoomDetails().getDescription());
            details.setRoom(room); // bidirectional
            room.setRoomDetails(details);
        }

        return room;
    }
}
