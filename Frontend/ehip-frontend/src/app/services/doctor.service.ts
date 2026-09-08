import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { DoctorDashboard } from '../models/doctor-dashboard.model';

import { DoctorPatient } from '../models/doctor-patient.model';

import { Observable } from 'rxjs/internal/Observable';

import { Doctor } from '../models/doctor.model';

import { DoctorSearch } from '../models/doctorSearch.model';

@Injectable({ providedIn: 'root' })
export class DoctorService {
  private apiUrl = 'http://localhost:8080/doctors';

  constructor(private http: HttpClient) {}

  getAllDoctors(): Observable<DoctorDashboard[]> {
    return this.http.get<DoctorDashboard[]>(this.apiUrl);
  }

  getDoctorDashboard(page: number = 0, size: number = 10, search: string = '') {
    return this.http.get<DoctorDashboard[]>(`${this.apiUrl}/dashboard`, {
      params: {
        page: page.toString(),
        size: size.toString(),
        search: search,
      },
    });
  }

  getDoctorPatients(doctorId: number): Observable<DoctorPatient[]> {
    return this.http.get<DoctorPatient[]>(`${this.apiUrl}/${doctorId}/patients`);
  }

  getDoctorById(doctorId: number): Observable<Doctor> {
    return this.http.get<Doctor>(`${this.apiUrl}/${doctorId}`);
  }

  addDoctor(doctor: any): Observable<any> {
    return this.http.post(this.apiUrl, doctor);
  }

  updateDoctor(doctorId: number, doctor: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${doctorId}`, doctor);
  }

  deleteDoctor(doctorId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${doctorId}`);
  }

  searchDoctors(search: string): Observable<DoctorSearch[]> {
    return this.http.get<DoctorSearch[]>(`${this.apiUrl}/search`, {
      params: {
        search: search,
      },
    });
  }
}
