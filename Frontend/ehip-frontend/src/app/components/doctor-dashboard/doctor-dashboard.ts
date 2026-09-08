import { Component, OnInit } from '@angular/core';

import { CommonModule } from '@angular/common';

import { TableModule } from 'primeng/table';

import { Toast } from 'primeng/toast';

import { ConfirmDialog } from 'primeng/confirmdialog';

import { ButtonModule } from 'primeng/button';

import { DialogModule } from 'primeng/dialog';

import { FormsModule } from '@angular/forms';

import { TagModule } from 'primeng/tag';

import { DoctorService } from '../../services/doctor.service';

import { DoctorDashboard } from '../../models/doctor-dashboard.model';

import { DoctorPatient } from '../../models/doctor-patient.model';

import { ChangeDetectorRef } from '@angular/core';

import { ConfirmationService, MessageService } from 'primeng/api';

import { Router } from '@angular/router';

import { PaginatorModule } from 'primeng/paginator';

@Component({
  selector: 'app-doctor-dashboard',

  standalone: true,

  imports: [
    CommonModule,

    TableModule,

    ButtonModule,

    DialogModule,

    TagModule,

    FormsModule,

    Toast,

    ConfirmDialog,

    PaginatorModule
  ],

  providers: [MessageService, ConfirmationService],

  templateUrl: './doctor-dashboard.html', //styleUrl: './doctor-dashboard.css'
})
export class DoctorDashboardComponent implements OnInit {
  doctors: DoctorDashboard[] = [];

  selectedDoctorPatients: DoctorPatient[] = [];

  selectedDoctorName: string = '';

  displayDialog: boolean = false;

  loading: boolean = false;

  totalRecords = 0;
  rows = 10;
  searchTerm: string='';
  first=0;

  selectedDoctor: any = {};

  displayDoctorDialog: boolean = false;

  displayAddDoctorDialog: boolean = false;

  displayEditDoctorDialog: boolean = false;

  isEditMode: boolean = false;

  submitAttempted: boolean = false;

  constructor(
    private doctorService: DoctorService,

    private cdr: ChangeDetectorRef,

    private messageService: MessageService,

    private confirmationService: ConfirmationService,

    private router: Router,
  ) {}

  goToPatients() {
    this.router.navigate(['/patients']);
  }

  ngOnInit() {
    this.loadDoctors(0,10);
  }

  loadDoctors(page: number = 0, size: number = this.rows) {
    this.loading = true;

    console.log(`Loading doctors: page=${page}, size=${size}`);

    this.doctorService.getDoctorDashboard(page, size, this.searchTerm).subscribe({
      next: (data) => {
        console.log('Doctors loaded:', data);

        this.doctors = data;

        if (data.length > 0) {
          this.totalRecords = data[0].totalRecords;
        } else {
          this.totalRecords = 0;
        }

        this.loading = false;
        this.cdr.detectChanges();
      },

      error: (err) => {
        console.error('Error loading doctors dashbaord', err);

        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  onPageChange(event: any) {
    this.first=event.first;
    const page = Math.floor(event.first / event.rows);
    this.rows = event.rows;
    this.loadDoctors(page, event.rows);
  }

  onSearchForDoc() : void{
    this.first=0;
    this.loadDoctors(0, this.rows);
  }

  clearSearchForDoc() {
  this.searchTerm = '';
  this.first=0;
  this.loadDoctors(0, this.rows);
}

  viewPatients(doctor: DoctorDashboard) {
    console.log('Viewing patients for doctor:', doctor);

    this.selectedDoctorName = doctor.doctorName;

    this.doctorService.getDoctorPatients(doctor.doctorId).subscribe({
      next: (data) => {
        this.selectedDoctorPatients = data;

        this.displayDialog = true;

        this.cdr.detectChanges();
      },

      error: (err) => {
        console.error('Error loading doctor patients:', err);
      },
    });
  }

  viewDoctorDetails(doctorId: number) {
    this.doctorService.getDoctorById(doctorId).subscribe((data) => {
      this.selectedDoctor = data;

      this.displayDoctorDialog = true;

      this.cdr.detectChanges();
    });
  }

  openAddDoctor() {
    this.selectedDoctor = {};

    this.isEditMode = false;

    this.submitAttempted = false;

    this.displayAddDoctorDialog = true;
  } //saveDoctor() {}

  saveDoctor() {
    this.submitAttempted = true;

    if (!this.selectedDoctor.firstName?.trim()) {
      this.messageService.add({
        severity: 'error',

        summary: 'Validation Error',

        detail: 'First name is required',
      });

      return;
    }

    const namePattern = /^[A-Za-z\s]+$/;

    if (!namePattern.test(this.selectedDoctor.firstName.trim())) {
      this.messageService.add({
        severity: 'error',

        summary: 'Validation Error',

        detail: 'First name must only contain letters',
      });

      return;
    }

    if (
      this.selectedDoctor.lastName?.trim() &&
      !namePattern.test(this.selectedDoctor.lastName.trim())
    ) {
      this.messageService.add({
        severity: 'error',

        summary: 'Validation Error',

        detail: 'Last name must only contain letters',
      });

      return;
    }

    if (!this.selectedDoctor.specialization?.trim()) {
      this.messageService.add({
        severity: 'error',

        summary: 'Validation Error',

        detail: 'Specialization is required',
      });

      return;
    }

    if (!namePattern.test(this.selectedDoctor.specialization.trim())) {
      this.messageService.add({
        severity: 'error',

        summary: 'Validation Error',

        detail: 'Specialization must only contain letters',
      });

      return;
    }

    if (!this.selectedDoctor.phone?.trim()) {
      this.messageService.add({
        severity: 'error',

        summary: 'Validation Error',

        detail: 'Phone number is required',
      });

      return;
    }

    const phonePattern = /^[1-9]\d{9}$/;

    if (!phonePattern.test(this.selectedDoctor.phone.trim())) {
      this.messageService.add({
        severity: 'error',

        summary: 'Validation Error',

        detail: 'Phone must be a valid 10-digit number',
      });

      return;
    }

    if (this.selectedDoctor.email?.trim()) {
      const emailPattern = /^[^0-9\s@][^\s@]*@[^\s@]+\.[^\s@]+$/;

      if (!emailPattern.test(this.selectedDoctor.email.trim())) {
        this.messageService.add({
          severity: 'error',

          summary: 'Validation Error',

          detail: 'Email must be valid and should not start with a number',
        });

        return;
      }
    } // lastName is optional — no check needed

    if (this.isEditMode) {
      this.doctorService.updateDoctor(this.selectedDoctor.doctorId, this.selectedDoctor).subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',

            summary: 'Updated',

            detail: 'Doctor updated successfully',
          });

          this.displayAddDoctorDialog = false;

          this.loadDoctors();
        },

        error: (err) => {
          this.messageService.add({
            severity: 'error',

            summary: 'Failed',

            detail: err?.error?.message || 'Could not update doctor',
          });
        },
      });
    } else {
      this.doctorService.addDoctor(this.selectedDoctor).subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',

            summary: 'Added',

            detail: 'Doctor added successfully',
          });

          this.displayAddDoctorDialog = false;

          this.loadDoctors();
        },

        error: (err) => {
          this.messageService.add({
            severity: 'error',

            summary: 'Failed',

            detail: err?.error?.message || 'Could not add doctor',
          });
        },
      });
    }
  }

  confirmDeleteDoctor(doctor: any) {
    console.log('Attempting to delete doctor:', doctor);

    this.confirmationService.confirm({
      message: `Are you sure you want to delete Dr. ${doctor.doctorName}? This action cannot be undone.`,

      header: 'Confirm Delete',

      icon: 'pi pi-exclamation-triangle',

      accept: () => {
        this.doctorService.deleteDoctor(doctor.doctorId).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',

              summary: 'Deleted',

              detail: `Dr. ${doctor.doctorName} deleted successfully`,
            });

            this.loadDoctors();
          },

          error: (err) => {
            // Backend rejected — likely has patients assigned

            const backendMessage = err?.error?.message || err?.error || '';

            if (err.status === 409 || backendMessage.toLowerCase().includes('patient')) {
              this.messageService.add({
                severity: 'error',

                summary: 'Cannot Delete',

                detail: `Dr. ${doctor.doctorName} has patients assigned. Please reassign or remove their patients before deleting.`,

                life: 6000,
              });
            } else {
              this.messageService.add({
                severity: 'error',

                summary: 'Failed',

                detail: `Could not delete Dr. ${doctor.doctorName}`,
              });
            }
          },
        });
      },
    });
  }

  editDoctor(doctorId: number) {
    console.log('Editing doctor with ID:', doctorId);

    this.doctorService.getDoctorById(doctorId).subscribe((data) => {
      console.log('Doctor data retrieved for editing:', data);

      this.selectedDoctor = { ...data };

      this.isEditMode = true;

      this.submitAttempted = false;

      this.displayAddDoctorDialog = true;

      this.cdr.detectChanges();
    });
  }
}
