package com.proiect.awbd.proiect_awbd.dto;

public class RoomDTO {
    private Long roomId;
    private String name;
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

