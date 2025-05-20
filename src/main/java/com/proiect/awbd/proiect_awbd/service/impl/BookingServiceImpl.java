package com.proiect.awbd.proiect_awbd.service.impl;

import com.proiect.awbd.proiect_awbd.dto.BookingRequestDTO;
import com.proiect.awbd.proiect_awbd.dto.BookingResponseDTO;
import com.proiect.awbd.proiect_awbd.dto.FeedbackDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.*;
import com.proiect.awbd.proiect_awbd.repository.*;
import com.proiect.awbd.proiect_awbd.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

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
        logger.info("Creating booking: {}", request);

        boolean alreadyExists = bookingRepository.existsByUser_UserIdAndRoom_RoomIdAndDateAndStartTimeAndEndTime(
                request.getUserId(), request.getRoomId(), request.getDate(),
                request.getStartTime(), request.getEndTime());

        if (alreadyExists) {
            logger.warn("Booking already exists for userId={}, roomId={}, date={}", request.getUserId(), request.getRoomId(), request.getDate());
            throw new RuntimeException("There is already a reservation for this user, room and date");
        }

        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> {
                    logger.error("User not found: {}", request.getUserId());
                    return new ResourceNotFoundException("User not found");
                });

        var room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> {
                    logger.error("Room not found: {}", request.getRoomId());
                    return new ResourceNotFoundException("Room not found");
                });

        var eventType = eventTypeRepository.findById(request.getEventTypeId())
                .orElseThrow(() -> {
                    logger.error("Event type not found: {}", request.getEventTypeId());
                    return new ResourceNotFoundException("Event type not found");
                });

        List<Long> equipmentIds = request.getEquipmentIds();
        List<Equipment> equipments = equipmentRepository.findAllById(equipmentIds);

        if (equipments.size() != equipmentIds.size()) {
            logger.error("Invalid equipment IDs provided: {}", equipmentIds);
            throw new ResourceNotFoundException("One or more equipment IDs are invalid");
        }

        Booking booking = new Booking();
        booking.setDate(request.getDate());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setUser(user);
        booking.setRoom(room);
        booking.setEventType(eventType);
        booking.setEquipments(equipments);

        Booking saved = bookingRepository.save(booking);
        logger.info("Booking created successfully: {}", saved.getBookingId());
        return mapToDTO(saved);
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        logger.info("Fetching all bookings");
        return bookingRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponseDTO getBookingById(Long id) {
        logger.info("Fetching booking by id: {}", id);
        return bookingRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> {
                    logger.error("Booking not found with id: {}", id);
                    return new ResourceNotFoundException("Booking not found");
                });
    }

    @Override
    public void deleteBooking(Long id) {
        logger.info("Deleting booking with id: {}", id);
        bookingRepository.deleteById(id);
    }

    @Override
    public Page<BookingResponseDTO> getAllBookingsPaginated(int page, int size, String sortBy, String direction) {
        logger.info("Fetching bookings with pagination: page={}, size={}, sortBy={}, direction={}", page, size, sortBy, direction);

        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return bookingRepository.findAll(pageRequest)
                .map(this::mapToDTO);
    }

    @Override
    public BookingResponseDTO updateBooking(Long bookingId, BookingRequestDTO request) {
        logger.info("Updating booking with id: {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    logger.error("Booking not found: {}", bookingId);
                    return new ResourceNotFoundException("Booking not found");
                });

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> {
                    logger.error("User not found: {}", request.getUserId());
                    return new ResourceNotFoundException("User not found");
                });

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> {
                    logger.error("Room not found: {}", request.getRoomId());
                    return new ResourceNotFoundException("Room not found");
                });

        EventType eventType = eventTypeRepository.findById(request.getEventTypeId())
                .orElseThrow(() -> {
                    logger.error("Event type not found: {}", request.getEventTypeId());
                    return new ResourceNotFoundException("Event type not found");
                });

        List<Long> equipmentIds = request.getEquipmentIds();
        List<Equipment> equipments = equipmentRepository.findAllById(equipmentIds);

        if (equipments.size() != equipmentIds.size()) {
            logger.error("Invalid equipment IDs during update: {}", equipmentIds);
            throw new ResourceNotFoundException("One or more equipment IDs are invalid");
        }

        booking.setDate(request.getDate());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setUser(user);
        booking.setRoom(room);
        booking.setEventType(eventType);
        booking.setEquipments(equipments);

        Booking updated = bookingRepository.save(booking);
        logger.info("Booking updated successfully: {}", updated.getBookingId());
        return mapToDTO(updated);
    }

    private BookingResponseDTO mapToDTO(Booking booking) {
        logger.debug("Mapping booking to DTO: {}", booking.getBookingId());

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
        dto.setUserId(booking.getUser().getUserId());
        dto.setRoomId(booking.getRoom().getRoomId());
        dto.setEventTypeId(booking.getEventType().getEventTypeId());
        dto.setEquipmentIds(booking.getEquipments().stream()
                .map(Equipment::getEquipmentId)
                .collect(Collectors.toList()));

        if (booking.getFeedback() != null) {
            logger.debug("Mapping feedback for booking: {}", booking.getBookingId());
            FeedbackDTO feedbackDTO = new FeedbackDTO();
            feedbackDTO.setBookingId(booking.getFeedback().getBookingId());
            feedbackDTO.setComment(booking.getFeedback().getComment());
            feedbackDTO.setRating(booking.getFeedback().getRating());
            dto.setFeedback(feedbackDTO);
        }

        return dto;
    }
}

