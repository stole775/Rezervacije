// src/app/services/company/company.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environment/environment';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {
  
  private apiUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) { }

  // Učitaj sve kompanije
  getAllCompanies(): Observable<any[]> { 
    return this.http.get<any[]>(`${environment.apiUrl}/api/companies`);//izmeni za pravu 
  }

   
}
