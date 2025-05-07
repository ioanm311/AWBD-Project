package com.proiect.awbd.proiect_awbd.service;

import com.proiect.awbd.proiect_awbd.model.RoomDetails;

import java.util.List;

public interface RoomDetailsService {
    RoomDetails saveRoomDetails(RoomDetails roomDetails);
    List<RoomDetails> getAllRoomDetails();
    RoomDetails getRoomDetailsById(Long id);
    void deleteRoomDetails(Long id);
}
