package com.proiect.awbd.proiect_awbd.service;

import com.proiect.awbd.proiect_awbd.dto.BookingRequestDTO;
import com.proiect.awbd.proiect_awbd.dto.BookingResponseDTO;
import com.proiect.awbd.proiect_awbd.model.Booking;
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
