package com.shivam.ehip.doctor;

public class DoctorSearchDTO {

    private int doctorId;
    private String doctorName;
    private String specialization;

    public DoctorSearchDTO() {
    }

    public DoctorSearchDTO(
            int doctorId,
            String doctorName,
            String specialization) {

        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.specialization = specialization;
    }

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
}