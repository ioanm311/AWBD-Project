package com.proiect.awbd.proiect_awbd.service.impl;

import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.RoomDetails;
import com.proiect.awbd.proiect_awbd.repository.RoomDetailsRepository;
import com.proiect.awbd.proiect_awbd.service.RoomDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomDetailsServiceImpl implements RoomDetailsService {

    private final RoomDetailsRepository roomDetailsRepository;

    public RoomDetailsServiceImpl(RoomDetailsRepository roomDetailsRepository) {
        this.roomDetailsRepository = roomDetailsRepository;
    }

    @Override
    public RoomDetails saveRoomDetails(RoomDetails roomDetails) {
        return roomDetailsRepository.save(roomDetails);
    }

    @Override
    public List<RoomDetails> getAllRoomDetails() {
        return roomDetailsRepository.findAll();
    }

    @Override
    public RoomDetails getRoomDetailsById(Long id) {
        return roomDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room details with id " + id + " not found!"));
    }

    @Override
    public void deleteRoomDetails(Long id) {
        if (!roomDetailsRepository.existsById(id)) {
            throw new ResourceNotFoundException("RoomDetails with id " + id + " not found");
        }
        roomDetailsRepository.deleteById(id);
    }

    @Override
    public RoomDetails updateRoomDetails(Long id, RoomDetails details) {
        RoomDetails existing = roomDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RoomDetails not found"));

        existing.setDescription(details.getDescription());
        existing.setEquipmentInfo(details.getEquipmentInfo());

        return roomDetailsRepository.save(existing);
    }
}
