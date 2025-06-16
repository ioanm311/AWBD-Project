package com.awbd.feedback_service.service.impl;

import com.awbd.feedback_service.dto.FeedbackDTO;
import com.awbd.feedback_service.exception.ResourceNotFoundException;
import com.awbd.feedback_service.model.Feedback;
import com.awbd.feedback_service.repository.FeedbackRepository;
import com.awbd.feedback_service.service.FeedbackService;
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

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    private FeedbackDTO toDTO(Feedback feedback) {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setBookingId(feedback.getBookingId());
        dto.setComment(feedback.getComment());
        dto.setRating(feedback.getRating());
        return dto;
    }

    private Feedback toEntity(FeedbackDTO dto) {
        Feedback feedback = new Feedback();
        feedback.setBookingId(dto.getBookingId());
        feedback.setComment(dto.getComment());
        feedback.setRating(dto.getRating());
        return feedback;
    }

    @Override
    public FeedbackDTO saveFeedback(FeedbackDTO dto) {
        logger.info("Saving feedback for bookingId {}", dto.getBookingId());

        if (feedbackRepository.existsByBookingId(dto.getBookingId())) {
            throw new IllegalStateException("Feedback already exists for bookingId: " + dto.getBookingId());
        }

        Feedback feedback = toEntity(dto);
        Feedback saved = feedbackRepository.save(feedback);
        return toDTO(saved);
    }

    @Override
    public FeedbackDTO updateFeedback(Long id, FeedbackDTO dto) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id: " + id));

        feedback.setComment(dto.getComment());
        feedback.setRating(dto.getRating());

        return toDTO(feedbackRepository.save(feedback));
    }

    @Override
    public FeedbackDTO getFeedbackById(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id: " + id));
        return toDTO(feedback);
    }

    @Override
    public void deleteFeedback(Long id) {
        if (!feedbackRepository.existsById(id)) {
            throw new ResourceNotFoundException("Feedback not found with id: " + id);
        }
        feedbackRepository.deleteById(id);
    }

    @Override
    public List<FeedbackDTO> getAllFeedbacks() {
        return feedbackRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<FeedbackDTO> getAllFeedbacksPaginated(Pageable pageable) {
        return feedbackRepository.findAll(pageable).map(this::toDTO);
    }
}