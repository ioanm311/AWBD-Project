package com.awbd.feedback_service.controller;

import com.awbd.feedback_service.dto.FeedbackDTO;
import com.awbd.feedback_service.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public FeedbackDTO createFeedback(@RequestBody @Valid FeedbackDTO dto) {
        return feedbackService.saveFeedback(dto);
    }

    @PutMapping("/{id}")
    public FeedbackDTO updateFeedback(@PathVariable Long id, @RequestBody @Valid FeedbackDTO dto) {
        return feedbackService.updateFeedback(id, dto);
    }

    @GetMapping("/{id}")
    public FeedbackDTO getFeedbackById(@PathVariable Long id) {
        return feedbackService.getFeedbackById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
    }

    @GetMapping
    public List<FeedbackDTO> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    @GetMapping("/paginated")
    public Page<FeedbackDTO> getPaginated(Pageable pageable) {
        return feedbackService.getAllFeedbacksPaginated(pageable);
    }
}
