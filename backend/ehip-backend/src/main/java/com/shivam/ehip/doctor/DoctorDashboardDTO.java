package com.shivam.ehip.doctor;

public class DoctorDashboardDTO {

    private int doctorId;
    private String doctorName;
    private String specialization;
    private int patientCount;
    private int highRiskPatientCount;
    private int totalRecords;

    public int getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
    public String getDoctorName() {
        return doctorName;
    }
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    public int getPatientCount() {
        return patientCount;
    }
    public void setPatientCount(int patientCount) {
        this.patientCount = patientCount;
    }
    public int getHighRiskPatientCount() {
        return highRiskPatientCount;
    }
    public void setHighRiskPatientCount(int highRiskPatientCount) {
        this.highRiskPatientCount = highRiskPatientCount;
    }
    public int getTotalRecords() {return totalRecords;}
    public void setTotalRecords(int totalRecords) {this.totalRecords = totalRecords;}
}
