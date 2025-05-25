package com.proiect.awbd.proiect_awbd.service.impl;

import com.proiect.awbd.proiect_awbd.dto.FeedbackDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.Booking;
import com.proiect.awbd.proiect_awbd.model.Feedback;
import com.proiect.awbd.proiect_awbd.repository.BookingRepository;
import com.proiect.awbd.proiect_awbd.repository.FeedbackRepository;
import com.proiect.awbd.proiect_awbd.service.FeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackServiceImpl.class);
    private final FeedbackRepository feedbackRepository;
    private final BookingRepository bookingRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, BookingRepository bookingRepository) {
        this.feedbackRepository = feedbackRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public FeedbackDTO saveFeedback(FeedbackDTO dto) {
        logger.info("Saving feedback for bookingId: {}", dto.getBookingId());

        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> {
                    logger.warn("Booking not found with id: {}", dto.getBookingId());
                    return new ResourceNotFoundException("Booking not found with id: " + dto.getBookingId());
                });

        if (feedbackRepository.existsByBookingId(dto.getBookingId())) {
            logger.warn("Feedback already exists for bookingId: {}", dto.getBookingId());
            throw new IllegalStateException("Feedback already exists for booking with id: " + dto.getBookingId());
        }

        Feedback feedback = new Feedback();
        feedback.setBooking(booking);
        feedback.setComment(dto.getComment());
        feedback.setRating(dto.getRating());

        Feedback saved = feedbackRepository.save(feedback);
        logger.info("Feedback saved successfully for bookingId: {}", saved.getBookingId());
        return toDTO(saved);
    }

    @Override
    public List<FeedbackDTO> getAllFeedbacks() {
        logger.info("Fetching all feedbacks from database");
        return feedbackRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<FeedbackDTO> getAllFeedbacksPaginated(Pageable pageable) {
        logger.info("Fetching feedbacks paginated and sorted");
        return feedbackRepository.findAll(pageable).map(this::toDTO);
    }

    @Override
    public FeedbackDTO getFeedbackById(Long id) {
        logger.info("Fetching feedback with id: {}", id);
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Feedback with id {} not found", id);
                    return new ResourceNotFoundException("Feedback with id " + id + " not found!");
                });
        return toDTO(feedback);
    }

    @Override
    public void deleteFeedback(Long bookingId) {
        logger.info("Deleting feedback with bookingId: {}", bookingId);
        if (!feedbackRepository.existsById(bookingId)) {
            logger.warn("Feedback with bookingId {} not found", bookingId);
            throw new ResourceNotFoundException("Feedback with bookingId " + bookingId + " not found");
        }
        feedbackRepository.deleteById(bookingId);
        logger.info("Feedback deleted with bookingId: {}", bookingId);
    }

    @Override
    public FeedbackDTO updateFeedback(Long id, FeedbackDTO dto) {
        logger.info("Updating feedback with id: {}", id);
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Feedback not found with id: {}", id);
                    return new ResourceNotFoundException("Feedback not found with id: " + id);
                });

        feedback.setComment(dto.getComment());
        feedback.setRating(dto.getRating());

        Feedback updated = feedbackRepository.save(feedback);
        logger.info("Feedback updated successfully for id: {}", updated.getBookingId());
        return toDTO(updated);
    }

    private FeedbackDTO toDTO(Feedback feedback) {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setBookingId(feedback.getBookingId());
        dto.setComment(feedback.getComment());
        dto.setRating(feedback.getRating());
        return dto;
    }
}
