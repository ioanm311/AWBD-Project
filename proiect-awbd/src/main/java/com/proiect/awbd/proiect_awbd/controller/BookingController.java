package com.proiect.awbd.proiect_awbd.controller;

import com.proiect.awbd.proiect_awbd.dto.BookingRequestDTO;
import com.proiect.awbd.proiect_awbd.dto.BookingResponseDTO;
import com.proiect.awbd.proiect_awbd.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingResponseDTO createBooking(@RequestBody BookingRequestDTO request) {
        logger.info("Received request to create booking for userId={}, roomId={}, date={}",
                request.getUserId(), request.getRoomId(), request.getDate());
        return bookingService.createBooking(request);
    }

    @GetMapping
    public List<BookingResponseDTO> getAllBookings() {
        logger.info("Fetching all bookings");
        return bookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public BookingResponseDTO getBookingById(@PathVariable Long id) {
        logger.info("Fetching booking with id={}", id);
        return bookingService.getBookingById(id);
    }

    @PutMapping("/{id}")
    public BookingResponseDTO updateBooking(@PathVariable Long id, @RequestBody BookingRequestDTO request) {
        logger.info("Updating booking with id={}, new date={}, roomId={}", id, request.getDate(), request.getRoomId());
        return bookingService.updateBooking(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
        logger.info("Deleting booking with id={}", id);
        bookingService.deleteBooking(id);
    }
}
