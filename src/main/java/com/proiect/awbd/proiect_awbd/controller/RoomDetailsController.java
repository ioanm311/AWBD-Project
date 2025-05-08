package com.proiect.awbd.proiect_awbd.controller;

import com.proiect.awbd.proiect_awbd.model.RoomDetails;
import com.proiect.awbd.proiect_awbd.service.RoomDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-details")
public class RoomDetailsController {

    private final RoomDetailsService roomDetailsService;

    public RoomDetailsController(RoomDetailsService roomDetailsService) {
        this.roomDetailsService = roomDetailsService;
    }

    @PostMapping
    public RoomDetails createRoomDetails(@RequestBody RoomDetails roomDetails) {
        return roomDetailsService.saveRoomDetails(roomDetails);
    }

    @GetMapping
    public List<RoomDetails> getAllRoomDetails() {
        return roomDetailsService.getAllRoomDetails();
    }

    @GetMapping("/{id}")
    public RoomDetails getRoomDetailsById(@PathVariable Long id) {
        return roomDetailsService.getRoomDetailsById(id);
    }

    @PutMapping("/{id}")
    public RoomDetails updateRoomDetails(@PathVariable Long id, @RequestBody RoomDetails roomDetails) {
        return roomDetailsService.updateRoomDetails(id, roomDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteRoomDetails(@PathVariable Long id) {
        roomDetailsService.deleteRoomDetails(id);
    }
}

