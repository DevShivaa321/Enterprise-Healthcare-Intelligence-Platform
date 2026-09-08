
import { Injectable } from '@angular/core'; 

import { HttpClient } from '@angular/common/http'; 

import { Observable } from 'rxjs'; 

import { RiskResult } from '../models/risk.model'; 

 

@Injectable({ providedIn: 'root' }) 

export class RiskService { 

  private baseUrl = 'http://localhost:8080/risk'; 

 

  constructor(private http: HttpClient) {} 

 

  calculateRisk(patientId: number): Observable<RiskResult> { 

    return this.http.get<RiskResult>(`${this.baseUrl}/${patientId}`); 

  } 

} 