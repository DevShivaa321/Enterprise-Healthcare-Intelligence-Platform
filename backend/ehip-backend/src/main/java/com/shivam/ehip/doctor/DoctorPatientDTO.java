package com.shivam.ehip.doctor;

public class DoctorPatientDTO {

    private int patientId;
    private String patientName;
    private Integer riskRating;

    public int getPatientId() {
        return patientId;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    public Integer getRiskRating() {
        return riskRating;
    }
    public void setRiskRating(Integer riskRating) {
        this.riskRating = riskRating;
    }


}
