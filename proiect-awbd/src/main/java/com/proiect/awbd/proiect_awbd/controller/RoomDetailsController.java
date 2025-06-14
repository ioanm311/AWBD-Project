package com.proiect.awbd.proiect_awbd.controller;

import com.proiect.awbd.proiect_awbd.dto.RoomDetailsDTO;
import com.proiect.awbd.proiect_awbd.service.RoomDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-details")
public class RoomDetailsController {

    private static final Logger logger = LoggerFactory.getLogger(RoomDetailsController.class);
    private final RoomDetailsService roomDetailsService;

    public RoomDetailsController(RoomDetailsService roomDetailsService) {
        this.roomDetailsService = roomDetailsService;
    }

    @PostMapping
    public ResponseEntity<RoomDetailsDTO> createRoomDetails(@RequestBody RoomDetailsDTO dto) {
        logger.info("Creating RoomDetails for roomId: {}", dto.getRoomId());
        return ResponseEntity.ok(roomDetailsService.saveRoomDetails(dto));
    }

    @GetMapping
    public ResponseEntity<List<RoomDetailsDTO>> getAllRoomDetails() {
        logger.info("Fetching all RoomDetails");
        return ResponseEntity.ok(roomDetailsService.getAllRoomDetails());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDetailsDTO> getRoomDetailsById(@PathVariable Long id) {
        logger.info("Fetching RoomDetails with id: {}", id);
        return ResponseEntity.ok(roomDetailsService.getRoomDetailsById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomDetailsDTO> updateRoomDetails(@PathVariable Long id, @RequestBody RoomDetailsDTO dto) {
        logger.info("Updating RoomDetails with id: {}", id);
        return ResponseEntity.ok(roomDetailsService.updateRoomDetails(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomDetails(@PathVariable Long id) {
        logger.info("Deleting RoomDetails with id: {}", id);
        roomDetailsService.deleteRoomDetails(id);
        return ResponseEntity.noContent().build();
    }
}