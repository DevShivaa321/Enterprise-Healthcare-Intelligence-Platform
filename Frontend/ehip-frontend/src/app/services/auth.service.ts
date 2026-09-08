import { Injectable } from '@angular/core'; 

import { HttpClient } from '@angular/common/http'; 

import { AuthRequest } from '../models/authRequest.model'; 

import { AuthResponse } from '../models/authResponse.model'; 

 

@Injectable({providedIn: 'root'}) 

export class AuthService { 

  private apiurl = 'http://localhost:8080/auth'; 

 

  constructor( private http: HttpClient) {} 

 

  Login(request: AuthRequest){ 

    return this.http.post<AuthResponse>(`${this.apiurl}/login`, request); 

  } 

 

  saveToken(token: string) { 

    localStorage.setItem('token', token); 

  } 

 

  getToken(){ 

    return localStorage.getItem('token'); 

  } 

 

  logout(){ 

    localStorage.removeItem('auth'); 

    localStorage.removeItem('token'); 

  } 

 

  isLoggedIn(){ 

    return this.getToken() !== null; 

  } 

 

  getRole(): string | null { 

    const token = this.getToken(); 

    if (!token) return null; 

    const payload = JSON.parse(atob(token.split('.')[1])); 

    return payload.role; 

  } 

} 
