package com.proiect.awbd.proiect_awbd.service;

import com.proiect.awbd.proiect_awbd.dto.EquipmentDTO;
import com.proiect.awbd.proiect_awbd.model.Equipment;

import java.util.List;

public interface EquipmentService {
    EquipmentDTO saveEquipment(EquipmentDTO equipment);
    List<EquipmentDTO> getAllEquipments();
    EquipmentDTO getEquipmentById(Long id);
    void deleteEquipment(Long id);
    EquipmentDTO updateEquipment(Long id, EquipmentDTO equipment);
}
