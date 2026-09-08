import { Routes } from '@angular/router'; 

import { LoginComponent } from './components/login/login'; 

import { PatientListComponent } from './components/patient-list/patient-list'; 

import { authGuard } from './guards/auth.guard'; 

import { DoctorDashboardComponent } from './components/doctor-dashboard/doctor-dashboard'; 

 

export const routes: Routes = [ 

  { path: '', redirectTo: '/login', pathMatch: 'full' }, 

  { path: 'login', component: LoginComponent }, 

  { path: 'patients', component: PatientListComponent, canActivate: [authGuard] }, 

  { path: 'doctors', component: DoctorDashboardComponent, canActivate: [authGuard] } 

];