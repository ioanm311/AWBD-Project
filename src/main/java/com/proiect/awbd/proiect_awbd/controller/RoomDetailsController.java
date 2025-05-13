package com.proiect.awbd.proiect_awbd.controller;

import com.proiect.awbd.proiect_awbd.dto.RoomDetailsDTO;
import com.proiect.awbd.proiect_awbd.model.RoomDetails;
import com.proiect.awbd.proiect_awbd.service.RoomDetailsService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<RoomDetailsDTO> createRoomDetails(@RequestBody RoomDetailsDTO dto) {
        return ResponseEntity.ok(roomDetailsService.saveRoomDetails(dto));
    }

    @GetMapping
    public ResponseEntity<List<RoomDetailsDTO>> getAllRoomDetails() {
        return ResponseEntity.ok(roomDetailsService.getAllRoomDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDetailsDTO> getRoomDetailsById(@PathVariable Long id) {
        return ResponseEntity.ok(roomDetailsService.getRoomDetailsById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomDetailsDTO> updateRoomDetails(@PathVariable Long id, @RequestBody RoomDetailsDTO dto) {
        return ResponseEntity.ok(roomDetailsService.updateRoomDetails(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomDetails(@PathVariable Long id) {
        roomDetailsService.deleteRoomDetails(id);
        return ResponseEntity.noContent().build();
    }
}

