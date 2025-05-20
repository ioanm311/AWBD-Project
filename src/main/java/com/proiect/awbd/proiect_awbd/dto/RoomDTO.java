package com.proiect.awbd.proiect_awbd.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RoomDTO {
    private Long roomId;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;

    private RoomDetailsDTO roomDetails;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public RoomDetailsDTO getRoomDetails() {
        return roomDetails;
    }

    public void setRoomDetails(RoomDetailsDTO roomDetails) {
        this.roomDetails = roomDetails;
    }
}

