package com.proiect.awbd.proiect_awbd.service;

import com.proiect.awbd.proiect_awbd.model.Feedback;

import java.util.List;

public interface FeedbackService {
    Feedback saveFeedback(Feedback feedback);
    List<Feedback> getAllFeedbacks();
    Feedback getFeedbackById(Long id);
    void deleteFeedback(Long id);
}
