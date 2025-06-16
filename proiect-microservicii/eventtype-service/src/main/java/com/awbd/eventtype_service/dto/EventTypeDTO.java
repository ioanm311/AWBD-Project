package com.awbd.eventtype_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EventTypeDTO {
    private Long eventTypeId;

    @NotNull(message = "Event type name cannot be null")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    public Long getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(Long eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
