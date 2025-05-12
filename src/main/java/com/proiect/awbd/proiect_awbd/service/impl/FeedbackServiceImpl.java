package com.proiect.awbd.proiect_awbd.service.impl;

import com.proiect.awbd.proiect_awbd.dto.FeedbackDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.Booking;
import com.proiect.awbd.proiect_awbd.model.Feedback;
import com.proiect.awbd.proiect_awbd.repository.BookingRepository;
import com.proiect.awbd.proiect_awbd.repository.FeedbackRepository;
import com.proiect.awbd.proiect_awbd.service.FeedbackService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final BookingRepository bookingRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, BookingRepository bookingRepository) {
        this.feedbackRepository = feedbackRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public FeedbackDTO saveFeedback(FeedbackDTO dto) {
        Feedback feedback = new Feedback();
        feedback.setBookingId(dto.getBookingId());
        feedback.setComment(dto.getComment());
        feedback.setRating(dto.getRating());

        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + dto.getBookingId()));

        feedback.setBooking(booking);
        return toDTO(feedbackRepository.save(feedback));
    }

    @Override
    public List<FeedbackDTO> getAllFeedbacks() {
        return feedbackRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FeedbackDTO getFeedbackById(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback with id " + id + " not found!"));
        return toDTO(feedback);
    }

    @Override
    public void deleteFeedback(Long id) {
        if (!feedbackRepository.existsById(id)) {
            throw new ResourceNotFoundException("Feedback with id " + id + " not found");
        }
        feedbackRepository.deleteById(id);
    }

    @Override
    public FeedbackDTO updateFeedback(Long id, FeedbackDTO dto) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id: " + id));

        feedback.setComment(dto.getComment());
        feedback.setRating(dto.getRating());

        return toDTO(feedbackRepository.save(feedback));
    }

    // === Conversii ===
    private FeedbackDTO toDTO(Feedback feedback) {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setBookingId(feedback.getBookingId());
        dto.setComment(feedback.getComment());
        dto.setRating(feedback.getRating());
        return dto;
    }
}
