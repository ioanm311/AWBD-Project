package com.proiect.awbd.proiect_awbd.service.impl;

import com.proiect.awbd.proiect_awbd.dto.BookingRequestDTO;
import com.proiect.awbd.proiect_awbd.dto.BookingResponseDTO;
import com.proiect.awbd.proiect_awbd.dto.FeedbackDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.Booking;
import com.proiect.awbd.proiect_awbd.model.Equipment;
import com.proiect.awbd.proiect_awbd.repository.*;
import com.proiect.awbd.proiect_awbd.service.BookingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final EventTypeRepository eventTypeRepository;
    private final EquipmentRepository equipmentRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserRepository userRepository,
                              RoomRepository roomRepository,
                              EventTypeRepository eventTypeRepository,
                              EquipmentRepository equipmentRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.eventTypeRepository = eventTypeRepository;
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO request) {
        boolean alreadyExists = bookingRepository.existsByUser_UserIdAndRoom_RoomIdAndDateAndStartTimeAndEndTime(
                request.getUserId(), request.getRoomId(), request.getDate(),
                request.getStartTime(), request.getEndTime());

        if (alreadyExists) {
            throw new RuntimeException("There is already a reservation for this user, room and date");
        }

        Booking booking = new Booking();
        booking.setDate(request.getDate());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());

        booking.setUser(userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
        booking.setRoom(roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found")));
        booking.setEventType(eventTypeRepository.findById(request.getEventTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Event type not found")));
        booking.setEquipments(equipmentRepository.findAllById(request.getEquipmentIds()));

        Booking saved = bookingRepository.save(booking);
        return mapToDTO(saved);
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponseDTO getBookingById(Long id) {
        return bookingRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    @Override
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public BookingResponseDTO updateBooking(Long id, BookingRequestDTO request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        booking.setDate(request.getDate());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setUser(userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
        booking.setRoom(roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found")));
        booking.setEventType(eventTypeRepository.findById(request.getEventTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Event type not found")));
        booking.setEquipments(equipmentRepository.findAllById(request.getEquipmentIds()));

        return mapToDTO(bookingRepository.save(booking));
    }

    private BookingResponseDTO mapToDTO(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setDate(booking.getDate());
        dto.setStartTime(booking.getStartTime());
        dto.setEndTime(booking.getEndTime());
        dto.setUsername(booking.getUser().getUsername());
        dto.setRoomName(booking.getRoom().getName());
        dto.setEventTypeName(booking.getEventType().getName());
        dto.setEquipmentNames(booking.getEquipments().stream()
                .map(Equipment::getName)
                .collect(Collectors.toList()));

        if (booking.getFeedback() != null) {
            FeedbackDTO feedbackDTO = new FeedbackDTO();
            feedbackDTO.setBookingId(booking.getFeedback().getBookingId());
            feedbackDTO.setComment(booking.getFeedback().getComment());
            feedbackDTO.setRating(booking.getFeedback().getRating());
            dto.setFeedback(feedbackDTO);
        }

        return dto;
    }
}
