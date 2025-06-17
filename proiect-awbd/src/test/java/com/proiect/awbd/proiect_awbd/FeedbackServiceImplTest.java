package com.proiect.awbd.proiect_awbd;

import com.proiect.awbd.proiect_awbd.dto.FeedbackDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.model.Booking;
import com.proiect.awbd.proiect_awbd.model.Feedback;
import com.proiect.awbd.proiect_awbd.repository.BookingRepository;
import com.proiect.awbd.proiect_awbd.repository.FeedbackRepository;
import com.proiect.awbd.proiect_awbd.service.impl.FeedbackServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceImplTest {

    @Mock  FeedbackRepository feedbackRepository;
    @Mock  BookingRepository bookingRepository;
    @InjectMocks
    FeedbackServiceImpl feedbackService;

    private FeedbackDTO sampleDto() {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setBookingId(1L);
        dto.setComment("Great session");
        dto.setRating(5);
        return dto;
    }

    @Test
    void saveFeedback_bookingNotFound_throws() {
        FeedbackDTO dto = sampleDto();
        // booking missing → service throws before it ever calls existsByBookingId()
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> feedbackService.saveFeedback(dto))
                .isInstanceOf(ResourceNotFoundException.class);

        verifyNoInteractions(feedbackRepository);
    }

    @Test
    void saveFeedback_duplicate_throws() {
        FeedbackDTO dto = sampleDto();
        Booking booking = new Booking(); booking.setBookingId(1L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(feedbackRepository.existsByBookingId(1L)).thenReturn(true);

        assertThatThrownBy(() -> feedbackService.saveFeedback(dto))
                .isInstanceOf(IllegalStateException.class);

        verify(feedbackRepository, never()).save(any());
    }

    @Test
    void saveFeedback_happyPath() {
        FeedbackDTO dto = sampleDto();
        Booking booking = new Booking(); booking.setBookingId(1L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(feedbackRepository.existsByBookingId(1L)).thenReturn(false);
        when(feedbackRepository.save(any(Feedback.class))).thenAnswer(inv -> {
            Feedback fb = inv.getArgument(0);
            fb.setBookingId(1L);         // mimic JPA populating the id
            return fb;
        });

        FeedbackDTO result = feedbackService.saveFeedback(dto);

        assertThat(result).isNotNull();
        assertThat(result.getBookingId()).isEqualTo(1L);
        assertThat(result.getComment()).isEqualTo("Great session");
        verify(feedbackRepository).save(any());
    }

    @Test
    void deleteFeedback_notFound_throws() {
        when(feedbackRepository.existsById(2L)).thenReturn(false);

        assertThatThrownBy(() -> feedbackService.deleteFeedback(2L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteFeedback_success() {
        when(feedbackRepository.existsById(3L)).thenReturn(true);

        feedbackService.deleteFeedback(3L);

        verify(feedbackRepository).deleteById(3L);
    }
}
