import { Component, OnInit } from '@angular/core';
import { FeedbackService } from 'src/app/services/feedback/feedback.service';

@Component({
  selector: 'app-feedback-admin',
  templateUrl: './feedback-admin.component.html',
  styleUrls: ['./feedback-admin.component.css']
})
export class FeedbackAdminComponent implements OnInit {
  feedbackList: any[] = [];
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(private feedbackService: FeedbackService) {}

  ngOnInit(): void {
    this.loadFeedback();
  }

  loadFeedback(): void {
    this.feedbackService.getAllFeedback().subscribe({
      next: (data) => {
        this.feedbackList = data;
      },
      error: (error) => {
        this.errorMessage = 'Failed to load feedback.';
        console.error(error);
      }
    });
  }

  deleteFeedback(feedbackId: number): void {
    this.feedbackService.deleteFeedback(feedbackId).subscribe({
      next: () => {
        this.successMessage = 'Feedback deleted successfully!';
        this.loadFeedback(); // Reload the feedback list after deletion
      },
      error: (error) => {
        this.errorMessage = 'Failed to delete feedback.';
        console.error(error);
      }
    });
  }
}
