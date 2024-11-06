package com.reservationbroker.reservation.services.impl;

import com.reservationbroker.reservation.entities.Feedback;
import com.reservationbroker.reservation.repositories.FeedbackRepository;
import com.reservationbroker.reservation.services.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {


    final private FeedbackRepository feedbackRepository;

    @Override
    public Feedback addFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public Feedback createFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public Feedback updateFeedback(Integer id, Feedback feedback) {
        return null;
    }

    @Override
    public void deleteFeedback(Integer id) {
         feedbackRepository.deleteById(Long.valueOf(id));
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return List.of();
    }

    @Override
    public Optional<Feedback> getFeedbackById(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<Feedback> getFeedbacksByUserId(Long userId) {
        return feedbackRepository.findByUserId(userId);
    }
}