import { Component, OnInit } from '@angular/core';

import { CommonModule } from '@angular/common';

import { FormsModule } from '@angular/forms';

import { PatientService } from '../../services/patient.service';

import { Patient } from '../../models/patient.model';

// import { PatientService } from '../../services/patient';

// PrimeNG Imports

import { TableModule } from 'primeng/table';

import { ButtonModule } from 'primeng/button';

import { DialogModule } from 'primeng/dialog';

import { InputTextModule } from 'primeng/inputtext';

import { Select, SelectModule } from 'primeng/select';

import { ToastModule } from 'primeng/toast';

import { ConfirmDialogModule } from 'primeng/confirmdialog';

import { TagModule } from 'primeng/tag';

import { MessageService, ConfirmationService } from 'primeng/api';

import { CardModule } from 'primeng/card';

import { ToolbarModule } from 'primeng/toolbar';

import { MedicalHistoryService } from '../../services/medicalHistory.service';

import { MedicalHistory } from '../../models/medicalHistory.model';

import { CheckboxModule } from 'primeng/checkbox';

import { RiskService } from '../../services/risk.service';

import { RiskResult } from '../../models/risk.model';

import { ChangeDetectorRef } from '@angular/core';

import { Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';

import { DoctorSearch } from '../../models/doctorSearch.model';
import { DoctorService } from '../../services/doctor.service';

import { AutoCompleteModule } from 'primeng/autocomplete';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';

import { Doctor } from '../../models/doctor.model';

@Component({
  selector: 'app-patient-list',

  standalone: true,

  imports: [
    CommonModule,

    FormsModule,

    TableModule,

    ButtonModule,

    DialogModule,

    InputTextModule,

    SelectModule,

    ToastModule,

    ConfirmDialogModule,

    TagModule,

    CardModule,

    ToolbarModule,

    CheckboxModule,

    AutoCompleteModule,
    InputGroupModule,
    InputGroupAddonModule,
  ],

  providers: [MessageService, ConfirmationService],

  templateUrl: './patient-list.html',
  //styleUrls: ['./patient-list.css'],
})
export class PatientListComponent implements OnInit {
  patients: Patient[] = [];

  doctorSearchResults: DoctorSearch[] = [];
  doctorLoading = false;

  selectedDoctor: DoctorSearch | null = null;

  displayDialog = false;

  isEditMode = false;

  loading = false;

  searchQuery: string = '';

  allPatients: Patient[] = []; // --- medical history ---

  totalRecords: number = 0;
  rows: number = 15;

  searchTerm: string = '';

  displayHistoryDialog: boolean = false;

  patientHistories: MedicalHistory[] = [];

  loadingHistory: boolean = false;

  addingHistory: boolean = false;

  calculatingRisk: boolean = false;

  medicalHistory: MedicalHistory = {
    id: 0,

    patientId: 0,

    sugarLevel: 0,

    hasDiabetes: false,

    bloodPressureSys: 0,

    bloodPressureDia: 0,

    heartRate: 0,

    cholesterol: 0,

    bmi: 0,

    smoking: false,

    alcoholConsumption: false,
  };

  selectedPatient: any = {};

  submitAttempted: boolean = false;

  riskResult: any = null;

  selectedMedicalHistory: any = null;

  genderOptions = [
    { label: 'Male', value: 'male' },

    { label: 'Female', value: 'female' },

    { label: 'Other', value: 'other' },
  ];

  doctors: any[] = [];

  doctorOptions: any[] = []; //this is what HTML expects

  today: string = new Date().toISOString().split('T')[0];

  userRole: string | null = ''; // Get the user role on component initialization

  constructor(
    private patientService: PatientService,

    private messageService: MessageService,

    private confirmationService: ConfirmationService,

    private medicalHistoryService: MedicalHistoryService,

    private riskService: RiskService,

    private cdr: ChangeDetectorRef,

    private router: Router,

    private authService: AuthService,

    private doctorService: DoctorService,
  ) {}

  ngOnInit(): void {
    this.userRole = this.authService.getRole(); // Get the user role on component initialization
    this.loadPatients(0, 15);
    //this.loadDoctors();
  }

  emptyPatient(): Patient {
    return { firstName: '', lastName: '', dateOfBirth: '', gender: '', doctorId: undefined };
  }

  loadPatients(page: number = 0, size: number = 15) {
    this.loading = true;

    this.patientService.getPatientDashboard(page, size, this.searchTerm).subscribe({
      next: (data: Patient[]) => {
        this.patients = data;
        this.allPatients = data; // saves the master copy for searching

        if (data.length > 0) {
          this.totalRecords = data[0].totalRecords || 0;
        } else {
          this.totalRecords = 0;
        }

        this.loading = false;
        this.cdr.detectChanges();

        // fetch risk for each patient after list loads
        // this.patients.forEach((p) => {
        //   this.riskService.calculateRisk(p.patientId!).subscribe({
        //     next: (risk) => {
        //       p.riskRating = risk.riskRating;

        //       this.cdr.detectChanges(); // update each row as risk arrives
        //     },
        //   });
        // });
      },

      error: (error) => {
        console.error('Error loading patient dashboard:', error);
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  onSearch(): void {
    this.loadPatients(0, this.rows);
  }

  clearSearch(): void {
    this.searchTerm = '';
    this.loadPatients(0, this.rows);
    this.cdr.detectChanges();
  }

  onPageChange(event: any) {
    const page = Math.floor(event.first / event.rows);
    this.rows = event.rows;
    this.loadPatients(page, event.rows);
  }

  loadDoctors() {
    this.doctorService.getAllDoctors().subscribe((data: any[]) => {
      this.doctors = data;

      this.cdr.detectChanges();
    });
  }

  goToDoctors() {
    this.router.navigate(['/doctors']);
  }

  searchDoctors(event: any): void {
    const query = event.filter?.trim();

    if (!query || query.length < 2) {
      if (this.selectedDoctor) {
        this.doctorSearchResults = [this.selectedDoctor];
      } else {
        this.doctorSearchResults = [];
      }

      return;
    }

    this.doctorService.searchDoctors(query).subscribe({
      next: (data: DoctorSearch[]) => {
        this.doctorSearchResults = data;
      },

      error: (err: any) => {
        console.error('Error searching doctors:', err);
        this.doctorSearchResults = [];
      },
    });
  }

  onDoctorSelect(doctor: DoctorSearch | null): void {
    console.log('Selected doctor:', doctor);
    if (doctor) {
      this.selectedDoctor = doctor;
      this.selectedPatient.doctorId = doctor.doctorId;
    } else {
      this.selectedDoctor = null;
      this.selectedPatient.doctorId = undefined;
    }
  }

  // getDoctorName(doctorId: number | undefined): string {
  //   if (!doctorId) return '-';

  //   const doctor = this.doctors.find((d) => d.doctorId === doctorId);

  //   return doctor ? doctor.doctorName : '-';
  // }

  openAddDialog(): void {
    this.isEditMode = false;
    this.selectedPatient = this.emptyPatient();
    this.selectedDoctor = null;
    this.displayDialog = true;
    this.submitAttempted = false;
  }

  openEditDialog(patient: Patient): void {
    console.log('Opening edit dialog:', patient);

    this.selectedPatient = { ...patient };
    this.isEditMode = true;
    this.submitAttempted = false;

    if (patient.doctorId != null) {
      const existingDoctor: DoctorSearch = {
        doctorId: patient.doctorId,
        doctorName: patient.doctorName || 'Unknown Doctor',
        specialization: patient.doctorSpecialization || 'Not Available',
      };

      this.doctorSearchResults = [existingDoctor];
      this.selectedDoctor = existingDoctor;

      console.log('Preloaded doctor:', this.selectedDoctor);
    } else {
      this.selectedDoctor = null;
      this.doctorSearchResults = [];
    }

    this.displayDialog = true;
  }

  savePatient(): void {
    console.log('Saving patient:', this.selectedPatient);

    this.submitAttempted = true; //only firstname, DOB and gender and doctor are required, so validate those before submitting

    //DOCTOR VALIDATION
    if (!this.selectedDoctor) {
      this.messageService.add({
        severity: 'error',
        summary: 'Validation Error',
        detail: 'Assigned doctor is required',
      });
      return;
    }

    // Copy selected doctor's ID to patient
    this.selectedPatient.doctorId = this.selectedDoctor.doctorId;

    if (!this.selectedPatient.firstName?.trim()) {
      this.messageService.add({
        severity: 'error',
        summary: 'Validation Error',
        detail: 'First name is required',
      });
      return;
    }

    const namePattern = /^[A-Za-z\s'-]+$/;
    if (!namePattern.test(this.selectedPatient.firstName.trim())) {
      this.messageService.add({
        severity: 'error',
        summary: 'Validation Error',
        detail: 'First name must only contain letters, spaces, apostrophes or hyphens',
      });
      return;
    }

    if (this.selectedPatient.firstName.trim().length < 2) {
      this.messageService.add({
        severity: 'error',
        summary: 'Validation Error',
        detail: 'First name must be at least 2 characters long',
      });
      return;
    }

    if (
      this.selectedPatient.lastName?.trim() &&
      !namePattern.test(this.selectedPatient.lastName.trim())
    ) {
      this.messageService.add({
        severity: 'error',
        summary: 'Validation Error',
        detail: 'Last name must only contain letters, spaces, apostrophes or hyphens',
      });
      return;
    }

    if (!this.selectedPatient.dateOfBirth) {
      this.messageService.add({
        severity: 'error',
        summary: 'Validation Error',
        detail: 'Date of birth is required',
      });
      return;
    }

    if (!this.selectedPatient.gender) {
      this.messageService.add({
        severity: 'error',
        summary: 'Validation Error',
        detail: 'Gender is required',
      });
      return;
    }

    if (this.isEditMode && this.selectedPatient.patientId) {
      this.patientService
        .updatePatient(this.selectedPatient.patientId, this.selectedPatient)
        .subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Success',
              detail: 'Patient updated successfully',
            });

            this.displayDialog = false;
            setTimeout(() => this.loadPatients(), 100);
          },

          error: () => {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Failed to update patient',
            });
          },
        });
    } else {
      this.patientService.addPatient(this.selectedPatient).subscribe({
        next: () => {
          this.messageService.add({
            severity: 'success',
            summary: 'Success',
            detail: 'Patient added successfully',
          });

          this.displayDialog = false;
          setTimeout(() => this.loadPatients(), 100);
        },

        error: () => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Failed to add patient',
          });
        },
      });
    }
  }

  confirmDelete(patient: Patient): void {
    this.confirmationService.confirm({
      message: `Are you sure you want to delete "${patient.firstName} ${patient.lastName ?? ''}"?`,
      header: 'Confirm Delete',
      icon: 'pi pi-exclamation-triangle',

      accept: () => {
        this.patientService.deletePatient(patient.patientId!).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Deleted',
              detail: `${patient.firstName} removed successfully`,
            });

            setTimeout(() => this.loadPatients(), 100);
          },

          error: () => {
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Failed to delete patient',
            });
          },
        });
      },
    });
  }

  viewPatient(id: number): void {
    this.patientService.getPatientById(id).subscribe({
      next: (data) => {
        this.selectedPatient = data;

        this.isEditMode = false;

        this.displayDialog = true;
      },

      error: () => {
        this.messageService.add({
          severity: 'error',

          summary: 'Error',

          detail: 'Failed to fetch patient details',
        });
      },
    });
  }

  // searchPatients(): void {
  //   if (!this.searchQuery.trim()) {
  //     this.patients = this.allPatients;

  //     return;
  //   }

  //   const query = this.searchQuery.toLowerCase().trim(); // Search by ID if query is a number

  //   if (!isNaN(Number(query))) {
  //     this.patients = this.allPatients.filter((p) => p.patientId === Number(query));
  //   } else {
  //     // Search by name

  //     this.patients = this.allPatients.filter(
  //       (p) =>
  //         p.firstName?.toLowerCase().includes(query) || p.lastName?.toLowerCase().includes(query),
  //     );
  //   }

  //   if (this.patients.length === 0) {
  //     this.messageService.add({
  //       severity: 'warn',

  //       summary: 'No Results',

  //       detail: `No patients found for "${this.searchQuery}"`,
  //     });
  //   }
  // }

  openHistoryDialog(patient: any) {
    this.selectedPatient = patient;

    this.displayHistoryDialog = true;

    this.riskResult = null;

    this.loadHistory();
  }

  loadHistory() {
    this.loadingHistory = true;

    this.medicalHistoryService.getAll().subscribe({
      next: (data) => {
        this.patientHistories = data.filter((h) => h.patientId === this.selectedPatient.patientId);

        this.loadingHistory = false;
      },

      error: () => (this.loadingHistory = false),
    });
  }

  validateMedicalHistory(): string | null {
    const h = this.medicalHistory;

    if (!h.sugarLevel || h.sugarLevel < 40 || h.sugarLevel > 600)
      return 'Sugar level must be between 40 and 600 mg/dL';

    if (!h.bloodPressureSys || h.bloodPressureSys < 50 || h.bloodPressureSys > 300)
      return 'Systolic BP must be between 50 and 300';

    if (!h.bloodPressureDia || h.bloodPressureDia < 30 || h.bloodPressureDia > 200)
      return 'Diastolic BP must be between 30 and 200';

    if (h.bloodPressureDia >= h.bloodPressureSys)
      return 'Diastolic BP must be less than Systolic BP';

    if (!h.heartRate || h.heartRate < 40 || h.heartRate > 220)
      return 'Heart rate must be between 40 and 220 bpm';

    if (!h.cholesterol || h.cholesterol < 50 || h.cholesterol > 500)
      return 'Cholesterol must be between 50 and 500';

    if (!h.bmi || h.bmi < 10 || h.bmi > 80) return 'BMI must be between 10 and 80';

    return null;
  } // addHistory() {
  //   if (!this.newSugarLevel) return;
  //   this.addingHistory = true;
  //   const payload = {
  //     patientId: this.selectedPatient.patientId,
  //     sugarLevel: this.newSugarLevel,
  //     hasDiabetes: this.newHasDiabetes
  //   };
  //   this.medicalHistoryService.add(payload).subscribe({
  //     next: () => {
  //       this.newSugarLevel = 0;
  //       this.newHasDiabetes = false;
  //       this.addingHistory = false;
  //       this.loadHistory();
  //       this.messageService.add({
  //         severity: 'success',
  //         summary: 'Record Added',
  //         detail: 'Medical history saved successfully'
  //       });
  //       this.cdr.detectChanges();
  //     },
  //     error: () => {
  //       this.addingHistory = false;
  //       this.messageService.add({
  //         severity: 'error',
  //         summary: 'Failed',
  //         detail: 'Could not save medical history'
  //       });
  //       this.cdr.detectChanges();
  //     }
  //   });
  // }

  addHistory() {
    //if (!this.medicalHistory.sugarLevel) return;  // use object field

    const validationError = this.validateMedicalHistory();

    if (validationError) {
      this.messageService.add({
        severity: 'error',

        summary: 'Validation Error',

        detail: validationError,
      });

      return;
    }

    this.addingHistory = true;

    const payload = {
      patientId: this.selectedPatient.patientId,

      sugarLevel: this.medicalHistory.sugarLevel,

      hasDiabetes: this.medicalHistory.hasDiabetes,

      bloodPressureSys: this.medicalHistory.bloodPressureSys,

      bloodPressureDia: this.medicalHistory.bloodPressureDia,

      heartRate: this.medicalHistory.heartRate,

      cholesterol: this.medicalHistory.cholesterol,

      bmi: this.medicalHistory.bmi,

      smoking: this.medicalHistory.smoking,

      alcoholConsumption: this.medicalHistory.alcoholConsumption,
    };

    this.medicalHistoryService.add(payload).subscribe({
      next: () => {
        // reset form fields

        this.medicalHistory = {
          id: this.selectedMedicalHistory?.id || null,

          patientId: this.selectedPatient.patientId,

          sugarLevel: 0,

          hasDiabetes: false,

          bloodPressureSys: 0,

          bloodPressureDia: 0,

          heartRate: 0,

          cholesterol: 0,

          bmi: 0,

          smoking: false,

          alcoholConsumption: false,
        };

        this.addingHistory = false;

        this.loadHistory();

        this.messageService.add({
          severity: 'success',

          summary: 'Record Added',

          detail: 'Medical history saved successfully',
        });

        this.cdr.detectChanges();
      },

      error: (err) => {
        if (err.status === 200 || err.status === 201 || err.status === 0) {
          //treat as success

          this.addingHistory = false;

          this.loadHistory();

          this.messageService.add({
            severity: 'success',

            summary: 'Record Added',

            detail: 'Medical history saved successfully',
          });
        } else {
          this.addingHistory = false;

          this.messageService.add({
            severity: 'error',

            summary: 'Failed',

            detail: 'Could not save medical history',
          });

          this.cdr.detectChanges();
        }
      },
    });
  } // calculateRisk() {
  //   this.calculatingRisk = true;
  //   this.riskResult = null;
  //   this.riskService.calculateRisk(this.selectedPatient.patientId!).subscribe({
  //     next: (data) => {
  //       this.riskResult = data;
  //       this.calculatingRisk = false;
  //       this.messageService.add({
  //         severity: data.category === 'HIGH' ? 'error' :
  //                   data.category === 'MEDIUM' ? 'warn' : 'success',
  //         summary: 'Risk Calculated',
  //         detail: `${this.selectedPatient.firstName} is ${data.category} risk (score: ${data.score}/6)`
  //       });
  //       this.cdr.detectChanges();
  //     },
  //     error: () => {
  //       this.calculatingRisk = false;
  //       this.messageService.add({
  //         severity: 'error',
  //         summary: 'Failed',
  //         detail: 'Could not calculate risk'
  //       });
  //       this.cdr.detectChanges();
  //     }
  //   });
  // }

  calculateRisk() {
    this.calculatingRisk = true;

    this.riskResult = null;

    this.riskService.calculateRisk(this.selectedPatient.patientId!).subscribe({
      next: (data) => {
        this.riskResult = data;

        this.calculatingRisk = false;

        this.messageService.add({
          severity: data.riskRating >= 7 ? 'error' : data.riskRating >= 4 ? 'warn' : 'success',
          summary: 'Risk Calculated',
          detail: `${this.selectedPatient.firstName} risk rating: ${data.riskRating}/10`,
        });

        this.cdr.detectChanges();
      },

      error: () => {
        this.calculatingRisk = false;

        this.messageService.add({
          severity: 'error',
          summary: 'Failed',
          detail: 'Could not calculate risk',
        });

        this.cdr.detectChanges();
      },
    });
  }

  sortByRisk() {
    //const order: Record<string, number> ={ 'HIGH':0 , 'MEDIUM':1, 'LOW':2  };

    this.patients.sort((a, b) => {
      const aScore = a.riskRating ?? 0; // Treat undefined as lowest

      const bScore = b.riskRating ?? 0;

      return bScore - aScore;
    });

    this.cdr.detectChanges();
  }

  logout() {
    this.authService.logout();

    this.router.navigate(['/login']);
  }

  onSelectPatient(event: any) {
    this.selectedPatient = event.data;

    this.medicalHistory.patientId = this.selectedPatient.id;

    this.loadHistory();

    this.calculateRisk();
  }
}
