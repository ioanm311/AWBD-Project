package com.awbd.booking_service.repository;

import com.awbd.booking_service.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByUserIdAndRoomIdAndDateAndStartTimeAndEndTime(
            Long userId, Long roomId, LocalDate date, LocalTime startTime, LocalTime endTime);

    boolean existsByUserIdAndRoomIdAndDateAndStartTimeAndEndTimeAndBookingIdNot(
            Long userId, Long roomId, LocalDate date, LocalTime startTime, LocalTime endTime, Long bookingId);
}

