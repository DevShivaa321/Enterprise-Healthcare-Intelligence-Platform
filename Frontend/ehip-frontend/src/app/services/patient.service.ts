
import { Injectable } from '@angular/core'; 

import { HttpClient } from '@angular/common/http'; 

import { Observable } from 'rxjs'; 

import { Patient } from '../models/patient.model'; 

 

@Injectable({ providedIn: 'root' }) 

export class PatientService { 

  private apiUrl = 'http://localhost:8080/patients'; 

  constructor(private http: HttpClient) {} 

  getAllPatients(): Observable<Patient[]> { 
    return this.http.get<Patient[]>(this.apiUrl); 
 } 

 getPatientDashboard( page: number, size: number, search: string=''): Observable<Patient[]> {
  return this.http.get<Patient[]>(
    `${this.apiUrl}/dashboard`, { params: { 
                    page: page.toString(),
                    size: size.toString(),
                    search: search
    }}
  );
}

 
  addPatient(patient: Patient): Observable<any> { 

    return this.http.post<any>(`${this.apiUrl}`, patient); 

  } 

  updatePatient(id: number, patient: Patient): Observable<any> { 

    return this.http.put(`${this.apiUrl}/${id}`, patient); 

  } 

  deletePatient(id: number): Observable<any> { 

    return this.http.delete(`${this.apiUrl}/${id}`); 

  }  

  getPatientById(id: number): Observable<Patient> { 

    return this.http.get<Patient>(`${this.apiUrl}/${id}`); 

  } 

} 