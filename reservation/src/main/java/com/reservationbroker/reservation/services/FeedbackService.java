package com.reservationbroker.reservation.services;

import com.reservationbroker.reservation.entities.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackService {
    Feedback addFeedback(Feedback feedback);
    Feedback createFeedback(Feedback feedback);
    Feedback updateFeedback(Integer id, Feedback feedback);
    void deleteFeedback(Integer id);
    List<Feedback> getAllFeedbacks();
    Optional<Feedback> getFeedbackById(Integer id);
    List<Feedback> getFeedbacksByUserId(Long userId);
}
