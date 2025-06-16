package com.awbd.room_service.service;

import com.awbd.room_service.dto.RoomDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomService {
    RoomDTO saveRoom(RoomDTO roomDTO);
    List<RoomDTO> getAllRooms();
    RoomDTO getRoomById(Long id);
    void deleteRoom(Long id);
    RoomDTO updateRoom(Long id, RoomDTO roomDTO);
    Page<RoomDTO> getAllRoomsPaginated(Pageable pageable);
}
