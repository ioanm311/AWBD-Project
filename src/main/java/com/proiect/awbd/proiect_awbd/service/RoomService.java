package com.proiect.awbd.proiect_awbd.service;

import com.proiect.awbd.proiect_awbd.dto.RoomDTO;
import com.proiect.awbd.proiect_awbd.model.Room;

import java.util.List;

public interface RoomService {
    RoomDTO saveRoom(RoomDTO roomDTO);
    List<RoomDTO> getAllRooms();
    RoomDTO getRoomById(Long id);
    void deleteRoom(Long id);
    RoomDTO updateRoom(Long id, RoomDTO roomDTO);
}