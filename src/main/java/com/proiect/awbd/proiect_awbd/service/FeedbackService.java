package com.proiect.awbd.proiect_awbd.service;

import com.proiect.awbd.proiect_awbd.dto.FeedbackDTO;
import com.proiect.awbd.proiect_awbd.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedbackService {
    FeedbackDTO saveFeedback(FeedbackDTO feedbackDTO);
    List<FeedbackDTO> getAllFeedbacks();
    FeedbackDTO getFeedbackById(Long id);
    void deleteFeedback(Long id);
    FeedbackDTO updateFeedback(Long id, FeedbackDTO feedbackDTO);
    Page<FeedbackDTO> getAllFeedbacksPaginated(Pageable pageable);
}
