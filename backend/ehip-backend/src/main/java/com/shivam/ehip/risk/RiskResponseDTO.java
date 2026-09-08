package com.shivam.ehip.risk;

public class RiskResponseDTO {
    private int patientId;
    private int riskRating;

    public int getPatientId() {
        return patientId;
    }
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
    public int getRiskRating() {
        return riskRating;
    }
    public void setRiskRating(int riskRating) {
        this.riskRating = riskRating;
    }
}
