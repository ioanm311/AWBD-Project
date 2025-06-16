package com.awbd.equipment_service.repository;

import com.awbd.equipment_service.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}
