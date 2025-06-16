package com.awbd.feedback_service.service;

import com.awbd.feedback_service.dto.FeedbackDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedbackService {
    FeedbackDTO saveFeedback(FeedbackDTO dto);
    FeedbackDTO updateFeedback(Long id, FeedbackDTO dto);
    FeedbackDTO getFeedbackById(Long id);
    void deleteFeedback(Long id);
    List<FeedbackDTO> getAllFeedbacks();
    Page<FeedbackDTO> getAllFeedbacksPaginated(Pageable pageable);
}
