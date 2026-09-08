package com.shivam.ehip.medicalHistory;

import java.time.LocalDate;

public class MedicalHistory {

    private int historyId;
    private int patientId;
    private double sugarLevel;
    private boolean hasDiabetes;
    private int bloodPressureSys;
    private int bloodPressureDia;

    private int heartRate;
    private int cholesterol;
    private double bmi;

    private boolean smoking;
    private boolean alcoholConsumption;
    private LocalDate recordedDate;

    public MedicalHistory() {}

    @SuppressWarnings("all")
    public MedicalHistory(int historyId, int patientId, double sugarLevel, boolean hasDiabetes,
                          int bloodPressureSys, int bloodPressureDia,
                          int heartRate, int cholesterol, double bmi,
                          boolean smoking, boolean alcoholConsumption,
                          LocalDate recordedDate) {
        super();
        this.historyId = historyId;
        this.patientId = patientId;
        this.sugarLevel = sugarLevel;
        this.hasDiabetes = hasDiabetes;
        this.bloodPressureSys= bloodPressureSys;
        this.bloodPressureDia= bloodPressureDia;
        this.heartRate= heartRate;
        this.cholesterol= cholesterol;
        this.bmi= bmi;
        this.smoking= smoking;
        this.alcoholConsumption= alcoholConsumption;
        this.recordedDate = recordedDate;
    }
    public int getId() {
        return historyId;
    }
    public void setId(int id) {
        this.historyId = id;
    }
    public int getPatientId() {
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
    public LocalDate getRecordedDate() {
        return recordedDate;
    }
    public void setRecordedDate(LocalDate recordedDate) {
        this.recordedDate = recordedDate;
    }
}
