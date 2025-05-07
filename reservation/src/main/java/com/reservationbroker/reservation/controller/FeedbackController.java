package com.reservationbroker.reservation.controller;

import com.reservationbroker.reservation.entities.Feedback;
import com.reservationbroker.reservation.entities.User;
import com.reservationbroker.reservation.services.FeedbackService;
import com.reservationbroker.reservation.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/feedback")
@AllArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final UserService userService;

    // Create feedback and capture the full User object from the authenticated user
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('SADMIN') or hasRole('CADMIN') or hasRole( 'WORKER')" )
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback, Authentication authentication) {
        // Extract username (or ID) from authentication and fetch the user object
        String username = authentication.getName();
        Optional<User> userOpt = userService.findByUsername(username);

        if (!userOpt.isPresent()) {
            return ResponseEntity.badRequest().body(null); // Return bad request if user not found
        }

        User user = userOpt.get();
        feedback.setUser(user); // Set the full User object for the feedback

        // Validate if the message is not null
        if (feedback.getMessage() == null || feedback.getMessage().isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Return bad request if message is missing
        }

        Feedback createdFeedback = feedbackService.createFeedback(feedback);
        return ResponseEntity.ok(createdFeedback);
    }



    // Get all feedback or feedback filtered by userId
    @GetMapping
    @PreAuthorize("hasRole('SADMIN')")
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {

            List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
            return ResponseEntity.ok(feedbacks);

    }

    // Get feedback by its ID (only accessible to SADMIN)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SADMIN')")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Integer id) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
        return feedback.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update feedback (can be done by the original user or admins)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('SADMIN') or hasRole('CADMIN') or hasRole( 'WORKER')")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable Integer id, @RequestBody Feedback feedback, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        Optional<Feedback> existingFeedback = feedbackService.getFeedbackById(id);

        // Check if the feedback exists and if the user is allowed to update it
        if (existingFeedback.isPresent() && (existingFeedback.get().getUser().getId().equals(userId) || authentication.getAuthorities().stream().anyMatch(
                role -> role.getAuthority().equals("ROLE_SADMIN") || role.getAuthority().equals("ROLE_CADMIN")))) {
            Feedback updatedFeedback = feedbackService.updateFeedback(id, feedback);
            return ResponseEntity.ok(updatedFeedback);
        } else {
            return ResponseEntity.status(403).body(null); // Forbidden if the user cannot update the feedback
        }
    }

    // Delete feedback (only SADMIN can delete feedback)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SADMIN')")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Integer id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }
}
