package com.proiect.awbd.proiect_awbd.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EquipmentDTO {
    private Long equipmentId;

    @NotNull(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

    