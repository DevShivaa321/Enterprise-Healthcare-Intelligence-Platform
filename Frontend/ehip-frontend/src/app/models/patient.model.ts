export interface Patient {
  patientId?: number;

  firstName: string;

  lastName?: string; // makes lastname optional field

  dateOfBirth: string;

  gender: string;

  doctorId?: number;

  doctorName?: string; // Optional field for doctor's name
  doctorSpecialization?: string;
  riskRating?: number | null; // Optional field for risk score
  totalRecords?: number;
 
}
