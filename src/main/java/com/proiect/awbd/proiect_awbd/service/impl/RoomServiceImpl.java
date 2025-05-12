package com.proiect.awbd.proiect_awbd.service.impl;

import com.proiect.awbd.proiect_awbd.dto.RoomDTO;
import com.proiect.awbd.proiect_awbd.dto.RoomDetailsDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.Room;
import com.proiect.awbd.proiect_awbd.model.RoomDetails;
import com.proiect.awbd.proiect_awbd.repository.RoomRepository;
import com.proiect.awbd.proiect_awbd.service.RoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public RoomDTO saveRoom(RoomDTO dto) {
        Room room = mapToEntity(dto);
        Room saved = roomRepository.save(room);
        return mapToDTO(saved);
    }

    @Override
    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoomDTO getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room with id " + id + " not found!"));
        return mapToDTO(room);
    }

    @Override
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room with id " + id + " not found");
        }
        roomRepository.deleteById(id);
    }

    @Override
    public RoomDTO updateRoom(Long id, RoomDTO dto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        room.setName(dto.getName());
        room.setCapacity(dto.getCapacity());

        if (dto.getRoomDetails() != null) {
            RoomDetails details = new RoomDetails();
            details.setDescription(dto.getRoomDetails().getDescription());
            details.setRoom(room); // bidirectional
            room.setRoomDetails(details);
        }

        Room updated = roomRepository.save(room);
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
