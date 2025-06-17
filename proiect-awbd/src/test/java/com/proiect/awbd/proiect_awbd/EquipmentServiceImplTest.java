package com.proiect.awbd.proiect_awbd;

import com.proiect.awbd.proiect_awbd.dto.EquipmentDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.Booking;
import com.proiect.awbd.proiect_awbd.model.Equipment;
import com.proiect.awbd.proiect_awbd.repository.EquipmentRepository;
import com.proiect.awbd.proiect_awbd.service.impl.EquipmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceImplTest {

    @Mock  EquipmentRepository equipmentRepository;
    @InjectMocks
    EquipmentServiceImpl equipmentService;

    @Test
    void saveEquipment_happyPath() {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setName("Projector");

        when(equipmentRepository.save(any(Equipment.class))).thenAnswer(inv -> {
            Equipment e = inv.getArgument(0);
            e.setEquipmentId(1L);
            return e;
        });

        EquipmentDTO saved = equipmentService.saveEquipment(dto);

        assertThat(saved.getEquipmentId()).isEqualTo(1L);
        assertThat(saved.getName()).isEqualTo("Projector");
        verify(equipmentRepository).save(any());
    }

    @Test
    void deleteEquipment_withBookings_throws() {
        Equipment eq = new Equipment();
        eq.setEquipmentId(5L);
        eq.setName("Speaker");
        eq.setBookings(List.of(new Booking()));           // non-empty

        when(equipmentRepository.findById(5L)).thenReturn(Optional.of(eq));

        assertThatThrownBy(() -> equipmentService.deleteEquipment(5L))
                .isInstanceOf(IllegalStateException.class);

        verify(equipmentRepository, never()).delete(any());
    }

    @Test
    void deleteEquipment_success() {
        Equipment eq = new Equipment();
        eq.setEquipmentId(6L);
        eq.setName("Speaker");
        eq.setBookings(Collections.emptyList());

        when(equipmentRepository.findById(6L)).thenReturn(Optional.of(eq));

        equipmentService.deleteEquipment(6L);

        verify(equipmentRepository).delete(eq);
    }

    @Test
    void updateEquipment_notFound_throws() {
        when(equipmentRepository.findById(88L)).thenReturn(Optional.empty());

        EquipmentDTO dto = new EquipmentDTO();
        dto.setName("Updated");

        assertThatThrownBy(() -> equipmentService.updateEquipment(88L, dto))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
