package com.proiect.awbd.proiect_awbd;

import com.proiect.awbd.proiect_awbd.model.Booking;
import com.proiect.awbd.proiect_awbd.model.Room;
import com.proiect.awbd.proiect_awbd.repository.RoomRepository;
import com.proiect.awbd.proiect_awbd.service.impl.RoomServiceImpl;
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
class RoomServiceImplTest {

    @Mock  RoomRepository roomRepository;
    @InjectMocks
    RoomServiceImpl roomService;

    @Test
    void deleteRoom_whenHasBookings_throws() {
        Room room = new Room();
        room.setRoomId(1L);
        room.setName("Conf-A");
        room.setCapacity(10);
        room.setBookings(List.of(new Booking()));   // simulate existing bookings

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        assertThatThrownBy(() -> roomService.deleteRoom(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("existing bookings");

        verify(roomRepository, never()).delete(any());
    }

    @Test
    void deleteRoom_success() {
        Room room = new Room();
        room.setRoomId(1L);
        room.setName("Conf-A");
        room.setCapacity(10);
        room.setBookings(Collections.emptyList());

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        roomService.deleteRoom(1L);

        verify(roomRepository).delete(room);
    }
}
