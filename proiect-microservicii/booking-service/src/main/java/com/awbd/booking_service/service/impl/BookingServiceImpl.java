package com.awbd.booking_service.service.impl;

import com.awbd.booking_service.dto.BookingRequestDTO;
import com.awbd.booking_service.dto.BookingResponseDTO;
import com.awbd.booking_service.exception.ResourceNotFoundException;
import com.awbd.booking_service.model.Booking;
import com.awbd.booking_service.repository.BookingRepository;
import com.awbd.booking_service.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO request) {
        logger.info("Creating booking: {}", request);

        boolean exists = bookingRepository.existsByUserIdAndRoomIdAndDateAndStartTimeAndEndTime(
                request.getUserId(), request.getRoomId(), request.getDate(),
                request.getStartTime(), request.getEndTime());

        if (exists) {
            throw new RuntimeException("A booking with the same details already exists.");
        }

        Booking booking = mapToEntity(request);
        Booking saved = bookingRepository.save(booking);
        return mapToResponseDTO(saved);
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BookingResponseDTO> getAllBookingsPaginated(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return bookingRepository.findAll(pageable).map(this::mapToResponseDTO);
    }

    @Override
    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with id " + id + " not found"));
        return mapToResponseDTO(booking);
    }

    @Override
    public BookingResponseDTO updateBooking(Long id, BookingRequestDTO request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with id " + id + " not found"));

        booking.setDate(request.getDate());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setUserId(request.getUserId());
        booking.setRoomId(request.getRoomId());
        booking.setEventTypeId(request.getEventTypeId());
        booking.setEquipmentIds(request.getEquipmentIds());

        Booking updated = bookingRepository.save(booking);
        return mapToResponseDTO(updated);
    }

    @Override
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with id " + id + " not found"));
        bookingRepository.delete(booking);
    }

    private BookingResponseDTO mapToResponseDTO(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setDate(booking.getDate());
        dto.setStartTime(booking.getStartTime());
        dto.setEndTime(booking.getEndTime());
        dto.setUserId(booking.getUserId());
        dto.setRoomId(booking.getRoomId());
        dto.setEventTypeId(booking.getEventTypeId());
        dto.setEquipmentIds(booking.getEquipmentIds());
        return dto;
    }

    private Booking mapToEntity(BookingRequestDTO request) {
        Booking booking = new Booking();
        booking.setDate(request.getDate());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setUserId(request.getUserId());
        booking.setRoomId(request.getRoomId());
        booking.setEventTypeId(request.getEventTypeId());
        booking.setEquipmentIds(request.getEquipmentIds());
        return booking;
    }
}