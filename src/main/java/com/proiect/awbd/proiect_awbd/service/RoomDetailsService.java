package com.proiect.awbd.proiect_awbd.service;

import com.proiect.awbd.proiect_awbd.dto.RoomDetailsDTO;
import com.proiect.awbd.proiect_awbd.model.RoomDetails;

import java.util.List;

public interface RoomDetailsService {
    RoomDetailsDTO saveRoomDetails(RoomDetailsDTO roomDetailsDTO);
    List<RoomDetailsDTO> getAllRoomDetails();
    RoomDetailsDTO getRoomDetailsById(Long id);
    void deleteRoomDetails(Long id);
    RoomDetailsDTO updateRoomDetails(Long id, RoomDetailsDTO roomDetailsDTO);
}
