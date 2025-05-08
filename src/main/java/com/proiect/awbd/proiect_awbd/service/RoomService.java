package com.proiect.awbd.proiect_awbd.service;

import com.proiect.awbd.proiect_awbd.model.Room;

import java.util.List;

public interface RoomService {
    Room saveRoom(Room room);
    List<Room> getAllRooms();
    Room getRoomById(Long id);
    void deleteRoom(Long id);
    Room updateRoom(Long id, Room room);
}