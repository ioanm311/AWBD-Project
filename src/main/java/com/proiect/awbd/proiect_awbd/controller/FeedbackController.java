package com.proiect.awbd.proiect_awbd.controller;

import com.proiect.awbd.proiect_awbd.dto.FeedbackDTO;
import com.proiect.awbd.proiect_awbd.model.Feedback;
import com.proiect.awbd.proiect_awbd.service.FeedbackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public FeedbackDTO saveFeedback(@RequestBody FeedbackDTO dto) {
        return feedbackService.saveFeedback(dto);
    }

    @GetMapping
    public List<FeedbackDTO> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @GetMapping("/{id}")
    public FeedbackDTO getFeedbackById(@PathVariable Long id) {
        return feedbackService.getFeedbackById(id);
    }

    @PutMapping("/{id}")
    public FeedbackDTO updateFeedback(@PathVariable Long id, @RequestBody FeedbackDTO dto) {
        return feedbackService.updateFeedback(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
    }
}