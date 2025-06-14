package com.proiect.awbd.proiect_awbd.controller;

import com.proiect.awbd.proiect_awbd.dto.FeedbackDTO;
import com.proiect.awbd.proiect_awbd.model.Feedback;
import com.proiect.awbd.proiect_awbd.service.FeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public FeedbackDTO saveFeedback(@RequestBody FeedbackDTO dto) {
        logger.info("Saving feedback for bookingId: {}", dto.getBookingId());
        return feedbackService.saveFeedback(dto);
    }

    @GetMapping
    public List<FeedbackDTO> getAllFeedbacks() {
        logger.info("Retrieving all feedbacks");
        return feedbackService.getAllFeedbacks();
    }

    @GetMapping("/{id}")
    public FeedbackDTO getFeedbackById(@PathVariable Long id) {
        logger.info("Retrieving feedback with id: {}", id);
        return feedbackService.getFeedbackById(id);
    }

    @PutMapping("/{id}")
    public FeedbackDTO updateFeedback(@PathVariable Long id, @RequestBody FeedbackDTO dto) {
        logger.info("Updating feedback with id: {}", id);
        return feedbackService.updateFeedback(id, dto);
    }

    @DeleteMapping("/{bookingId}")
    public void deleteFeedback(@PathVariable Long bookingId) {
        logger.info("Deleting feedback with bookingId: {}", bookingId);
        feedbackService.deleteFeedback(bookingId);
    }
}