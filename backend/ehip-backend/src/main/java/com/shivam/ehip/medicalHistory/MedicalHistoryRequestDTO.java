package com.shivam.ehip.medicalHistory;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MedicalHistoryRequestDTO {

    @NotNull(message="Patient ID is required")
    private Integer patientId;

    @NotNull(message="SugarLevel is required")
    @Min(value=40, message="Sugar Level must be atleast 40")
    @Max(value=600, message="Sugar Level cannot exceed 600")
    private double sugarLevel;

    @NotNull(message="Diabetes field is required")
    private boolean hasDiabetes;

    @NotNull(message="Blood Pressure Systolic is required")
    @Min(value=50, message="Systolic BP must be atleast 50")
    @Max(value=300, message="Systolic BP cannot exceed 300")
    private Integer bloodPressureSys;

    @NotNull(message="Blood Pressure Diastolic is required")
    @Min(value=30, message="Diastolic BP must be atleast 30")
    @Max(value=200, message="Diastolic BP cannot exceed 200")
    private Integer bloodPressureDia;

    @NotNull(message="Heart rate is required")
    @Min(value=40, message="Heart rate must be atleast 40")
    @Max(value=220, message="Heart rate cannot exceed 220")
    private Integer heartRate;

    @NotNull(message="Cholesterol is required")
    @Min(value=50, message="Cholesterol must be atleast 50")
    @Max(value=500, message="Cholesterol cannot exceed 500")
    private Integer cholesterol;

    @NotNull(message="BMI is required")
    @DecimalMin(value="10.0", message="BMI must be atleast 10")
    @DecimalMax(value="80.0", message="BMI cannot exceed 80")
    private double bmi;

    @NotNull(message="Smoking field is required")
    private boolean smoking;

    @NotNull(message="AlcoholConsumption is required")
    private boolean alcoholConsumption;

    public Integer getPatientId() {
        return patientId;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    public double getSugarLevel() {
        return sugarLevel;
    }
    public void setSugarLevel(double sugarLevel) {
        this.sugarLevel = sugarLevel;
    }
    public boolean isHasDiabetes() {
        return hasDiabetes;
    }
    public void setHasDiabetes(boolean hasDiabetes) {
        this.hasDiabetes = hasDiabetes;
    }
    public int getBloodPressureSys() {
        return bloodPressureSys;
    }
    public void setBloodPressureSys(int bloodPressureSys) {
        this.bloodPressureSys = bloodPressureSys;
    }
    public int getBloodPressureDia() {
        return bloodPressureDia;
    }
    public void setBloodPressureDia(int bloodPressureDia) {
        this.bloodPressureDia = bloodPressureDia;
    }
    public int getHeartRate() {
        return heartRate;
    }
    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }
    public int getCholesterol() {
        return cholesterol;
    }
    public void setCholesterol(int cholesterol) {
        this.cholesterol = cholesterol;
    }
    public double getBmi() {
        return bmi;
    }
    public void setBmi(double bmi) {
        this.bmi = bmi;
    }
    public boolean isSmoking() {
        return smoking;
    }
    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }
    public boolean isAlcoholConsumption() {
        return alcoholConsumption;
    }
    public void setAlcoholConsumption(boolean alcoholConsumption) {
        this.alcoholConsumption = alcoholConsumption;
    }
}
