package com.proiect.awbd.proiect_awbd.service;

import com.proiect.awbd.proiect_awbd.model.Equipment;

import java.util.List;

public interface EquipmentService {
    Equipment saveEquipment(Equipment equipment);
    List<Equipment> getAllEquipments();
    Equipment getEquipmentById(Long id);
    void deleteEquipment(Long id);
}
