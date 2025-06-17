package com.proiect.awbd.proiect_awbd;

import com.proiect.awbd.proiect_awbd.dto.BookingRequestDTO;
import com.proiect.awbd.proiect_awbd.dto.BookingResponseDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.*;
import com.proiect.awbd.proiect_awbd.repository.*;
import com.proiect.awbd.proiect_awbd.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock BookingRepository bookingRepository;
    @Mock UserRepository userRepository;
    @Mock RoomRepository roomRepository;
    @Mock EventTypeRepository eventTypeRepository;
    @Mock EquipmentRepository equipmentRepository;

    @InjectMocks
    BookingServiceImpl bookingService;

    private BookingRequestDTO newRequest() {
        BookingRequestDTO req = new BookingRequestDTO();
        req.setUserId(1L);
        req.setRoomId(1L);
        req.setEventTypeId(1L);
        req.setDate(LocalDate.now());
        req.setStartTime(LocalTime.of(9,0));
        req.setEndTime(LocalTime.of(10,0));
        req.setEquipmentIds(List.of());
        return req;
    }

    @Test
    void createBooking_whenDuplicate_throws() {
        BookingRequestDTO req = newRequest();
        when(bookingRepository.existsByUser_UserIdAndRoom_RoomIdAndDateAndStartTimeAndEndTime(
                anyLong(), anyLong(), any(), any(), any())).thenReturn(true);

        assertThatThrownBy(() -> bookingService.createBooking(req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("already a reservation");

        verifyNoMoreInteractions(userRepository, roomRepository, eventTypeRepository, equipmentRepository);
    }

    @Test
    void createBooking_userMissing_throws() {
        BookingRequestDTO req = newRequest();
        when(bookingRepository.existsByUser_UserIdAndRoom_RoomIdAndDateAndStartTimeAndEndTime(
                anyLong(), anyLong(), any(), any(), any())).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.createBooking(req))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void createBooking_happyPath() {
        BookingRequestDTO req = newRequest();
        req.setEquipmentIds(List.of(1L,2L));

        when(bookingRepository.existsByUser_UserIdAndRoom_RoomIdAndDateAndStartTimeAndEndTime(
                1L,1L,req.getDate(),req.getStartTime(),req.getEndTime())).thenReturn(false);

        User user = new User(); user.setUserId(1L);
        Room room = new Room(); room.setRoomId(1L);
        EventType et = new EventType(); et.setEventTypeId(1L);
        Equipment e1 = new Equipment(); e1.setEquipmentId(1L);
        Equipment e2 = new Equipment(); e2.setEquipmentId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(eventTypeRepository.findById(1L)).thenReturn(Optional.of(et));
        when(equipmentRepository.findAllById(List.of(1L,2L))).thenReturn(List.of(e1,e2));
        when(bookingRepository.save(any())).thenAnswer(inv -> {
            Booking b = inv.getArgument(0);
            b.setBookingId(7L);
            return b;
        });

        BookingResponseDTO resp = bookingService.createBooking(req);

        assertThat(resp.getBookingId()).isEqualTo(7L);
        assertThat(resp.getRoomId()).isEqualTo(1L);
        verify(bookingRepository).save(any());
    }
}
