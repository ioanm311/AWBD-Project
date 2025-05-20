package com.proiect.awbd.proiect_awbd.repository;

import com.proiect.awbd.proiect_awbd.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByUser_UserIdAndRoom_RoomIdAndDateAndStartTimeAndEndTime(
            Long userId, Long roomId, LocalDate date, LocalTime startTime, LocalTime endTime);

    boolean existsByUser_UserIdAndRoom_RoomIdAndDateAndStartTimeAndEndTimeAndBookingIdNot(
            Long userId, Long roomId, LocalDate date, LocalTime startTime, LocalTime endTime, Long bookingId);
}
