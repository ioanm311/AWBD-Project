package com.awbd.roomdetails_service.service;

import com.awbd.roomdetails_service.dto.RoomDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomDetailsService {
    RoomDetailsDTO saveRoomDetails(RoomDetailsDTO dto);
    List<RoomDetailsDTO> getAllRoomDetails();
    RoomDetailsDTO getRoomDetailsById(Long id);
    void deleteRoomDetails(Long id);
    RoomDetailsDTO updateRoomDetails(Long id, RoomDetailsDTO dto);
    Page<RoomDetailsDTO> getPaginatedRoomDetails(Pageable pageable);
}
