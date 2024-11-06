import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs'; 
import { environment } from 'src/environment/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  // Dobavljanje korisnika po ID-u
  getUserById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  // Dobavljanje svih korisnika
  getAllUsers(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  // Ažuriranje korisnika
  updateUser(userId: number, userData: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${userId}`, userData);
  }
  // Dobavljanje korisnika po ID-u kompanije
  getUsersByCompanyId(companyId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/companyId/${companyId}`);
  }

  

  // Blokiranje korisnika
  blockUser(userId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/${userId}/block`, {});
  }

  // Odblokiranje korisnika
  unblockUser(userId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/${userId}/unblock`, {});
  }

   

  // Dodeljivanje uloge korisniku
   
  
  assignRole(roleData: { userId: number, roleName: string, companyId: number }): Observable<any> {
    return this.http.post(`${environment.apiUrl}/roles/assign`, roleData);
  }
  
  
  changePassword(passwordData: { userId: number, newPassword: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/change-password`, passwordData);
  }
  

}
