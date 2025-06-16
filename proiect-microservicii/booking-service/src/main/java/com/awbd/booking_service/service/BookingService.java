package com.awbd.booking_service.service;

import com.awbd.booking_service.dto.BookingRequestDTO;
import com.awbd.booking_service.dto.BookingResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookingService {
    BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO);
    List<BookingResponseDTO> getAllBookings();
    BookingResponseDTO getBookingById(Long id);
    void deleteBooking(Long id);
    BookingResponseDTO updateBooking(Long id, BookingRequestDTO bookingRequestDTO);
    Page<BookingResponseDTO> getAllBookingsPaginated(int page, int size, String sortBy, String direction);
}