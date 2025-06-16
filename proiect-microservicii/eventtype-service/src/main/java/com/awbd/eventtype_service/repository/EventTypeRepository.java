package com.awbd.eventtype_service.repository;

import com.awbd.eventtype_service.model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventTypeRepository extends JpaRepository<EventType, Long> {
}
