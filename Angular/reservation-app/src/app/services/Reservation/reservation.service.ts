import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from 'src/environment/environment';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  private apiUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) {}

  getAllReservations(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/reservations`);
  }

 
  updateReservation(id: number, reservation: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/reservations/${id}`, reservation);
  }
  
  getUserReservations(userId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/reservations/user/${userId}`);
  }
  deleteReservation(reservationId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/reservations/${reservationId}`);
  }
  createReservation(reservationData: any): Observable<any> {
    return this.http.post(`${environment.apiUrl}/reservations`, reservationData);
  }
  // ReservationService.ts

getUserServices(userId: number): Observable<any> {
  return this.http.get(`${environment.apiUrl}/user-usluge/user/${userId}/usluge2`);
}
 
getUserReservationsByCompanyid(companyId: number): Observable<any> {
  return this.http.get(`${environment.apiUrl}/reservations/user/companyId/${companyId}`);
}


 
 //dodati da vraca sve rezervacije na osnovu id kompanije za cadmina
 
}
