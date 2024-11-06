import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs'; 
import { environment } from 'src/environment/environment';

@Injectable({
  providedIn: 'root'
})
export class PendingReservationService {
  private apiUrl = `${environment.apiUrl}/reservations/pending`;

  constructor(private http: HttpClient) {}

  getPendingReservations(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  approveReservation(reservationId: number, approvalData: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${reservationId}/approve`, approvalData);
  }

  rejectReservation(reservationId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${reservationId}/reject`, {});
  }
}
