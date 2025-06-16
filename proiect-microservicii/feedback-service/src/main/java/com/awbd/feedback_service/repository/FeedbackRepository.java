package com.awbd.feedback_service.repository;

import com.awbd.feedback_service.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    boolean existsByBookingId(Long bookingId);
}
