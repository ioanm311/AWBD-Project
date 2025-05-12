package com.proiect.awbd.proiect_awbd.service.impl;

import com.proiect.awbd.proiect_awbd.dto.RoomDetailsDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.Room;
import com.proiect.awbd.proiect_awbd.model.RoomDetails;
import com.proiect.awbd.proiect_awbd.repository.RoomDetailsRepository;
import com.proiect.awbd.proiect_awbd.repository.RoomRepository;
import com.proiect.awbd.proiect_awbd.service.RoomDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomDetailsServiceImpl implements RoomDetailsService {

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
                .orElseThrow(() -> new ResourceNotFoundException("Room with id " + dto.getRoomId() + " not found"));
        entity.setRoom(room);
        return entity;
    }

    @Override
    public RoomDetailsDTO saveRoomDetails(RoomDetailsDTO dto) {
        RoomDetails entity = mapToEntity(dto);
        return mapToDTO(roomDetailsRepository.save(entity));
    }

    @Override
    public List<RoomDetailsDTO> getAllRoomDetails() {
        return roomDetailsRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoomDetailsDTO getRoomDetailsById(Long id) {
        RoomDetails entity = roomDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RoomDetails with id " + id + " not found"));
        return mapToDTO(entity);
    }

    @Override
    public void deleteRoomDetails(Long id) {
        if (!roomDetailsRepository.existsById(id)) {
            throw new ResourceNotFoundException("RoomDetails with id " + id + " not found");
        }
        roomDetailsRepository.deleteById(id);
    }

    @Override
    public RoomDetailsDTO updateRoomDetails(Long id, RoomDetailsDTO dto) {
        RoomDetails entity = roomDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RoomDetails not found"));

        entity.setDescription(dto.getDescription());
        entity.setEquipmentInfo(dto.getEquipmentInfo());

        // If the room is changed
        if (!entity.getRoomId().equals(dto.getRoomId())) {
            Room newRoom = roomRepository.findById(dto.getRoomId())
                    .orElseThrow(() -> new ResourceNotFoundException("Room with id " + dto.getRoomId() + " not found"));
            entity.setRoom(newRoom);
            entity.setRoomId(dto.getRoomId());
        }

        return mapToDTO(roomDetailsRepository.save(entity));
    }
}
