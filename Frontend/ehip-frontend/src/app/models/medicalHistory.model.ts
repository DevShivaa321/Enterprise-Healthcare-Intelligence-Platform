export interface MedicalHistory {
  id: number;

  patientId: number;

  sugarLevel: number;

  hasDiabetes: boolean;

  bloodPressureSys: number;

  bloodPressureDia: number;

  heartRate: number;

  cholesterol: number;

  bmi: number;

  smoking: boolean;

  alcoholConsumption: boolean;
}
