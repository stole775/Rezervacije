import { Component, OnInit } from '@angular/core'; 
import { FeedbackService } from 'src/app/services/feedback/feedback.service';
import { AuthService } from 'src/app/services/auth/auth.service'; // To get the user ID
import { Router } from '@angular/router';

@Component({
  selector: 'app-feedback',
  templateUrl: './feedback.component.html',
  styleUrls: ['./feedback.component.css']
})
export class FeedbackComponent implements OnInit {
  feedbackText: string = '';
  successMessage: string | null = null;
  errorMessage: string | null = null;
  userId: number | null = null;

  constructor(
    private feedbackService: FeedbackService,
     private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (!this.authService.isLoggedIn()) {
      this.authService.logout();
      this.router.navigate(['/login']);
    } else {
    // Get the user ID from AuthService when the component is initialized
    this.userId = this.authService.getUserId();
  }
  }

  submitFeedback(): void {
    if (!this.userId) {
      this.errorMessage = 'User not authenticated.';
      return;
    }
  
    const feedbackData = { 
      message: this.feedbackText, // Send 'message' to match backend field
      userId: this.userId // Include the user ID in the payload
    };
  
    if (this.feedbackText.trim() !== "") {
      this.feedbackService.submitFeedback(feedbackData).subscribe({
        next: () => {
          this.successMessage = 'Hvala vam na izdvojenom vremenu za feedback!';
          this.feedbackText = ''; // Reset the form
        },
        error: (error) => {
          this.errorMessage = 'Neuspesno slanje feedbacka.';
          console.error(error);
        }
      });
    } else {
      this.errorMessage = 'Polje poruke je prazno.';
    }
  }
  
  
}
