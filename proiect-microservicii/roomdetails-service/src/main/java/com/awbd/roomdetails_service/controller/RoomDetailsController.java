package com.awbd.roomdetails_service.controller;

import com.awbd.roomdetails_service.dto.RoomDetailsDTO;
import com.awbd.roomdetails_service.service.RoomDetailsService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-details")
public class RoomDetailsController {

    private final RoomDetailsService service;

    public RoomDetailsController(RoomDetailsService service) {
        this.service = service;
    }

    @PostMapping
    public RoomDetailsDTO createRoomDetails(@RequestBody @Valid RoomDetailsDTO dto) {
        return service.saveRoomDetails(dto);
    }

    @GetMapping
    public List<RoomDetailsDTO> getAllRoomDetails() {
        return service.getAllRoomDetails();
    }

    @GetMapping("/{id}")
    public RoomDetailsDTO getRoomDetailsById(@PathVariable Long id) {
        return service.getRoomDetailsById(id);
    }

    @PutMapping("/{id}")
    public RoomDetailsDTO updateRoomDetails(@PathVariable Long id, @RequestBody @Valid RoomDetailsDTO dto) {
        return service.updateRoomDetails(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteRoomDetails(@PathVariable Long id) {
        service.deleteRoomDetails(id);
    }

    @GetMapping("/page")
    public Page<RoomDetailsDTO> getPaginatedRoomDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "roomId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return service.getPaginatedRoomDetails(PageRequest.of(page, size, sort));
    }
}
