import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs'; 
import { environment } from 'src/environment/environment';

@Injectable({
  providedIn: 'root'
})
export class FeedbackService {
  private apiUrl = `${environment.apiUrl}/feedback`;

  constructor(private http: HttpClient) {}

  submitFeedback(feedbackData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}`, feedbackData);
  }

  // Get all feedback (for SAdmin)
  getAllFeedback(): Observable<any> {
    return this.http.get(`${this.apiUrl}`);
  }

  // Delete feedback
  deleteFeedback(feedbackId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${feedbackId}`);
  }
}
