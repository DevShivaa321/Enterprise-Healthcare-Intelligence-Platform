package com.shivam.ehip.patient;

import java.time.LocalDate;

public class PatientDashboardDTO {

    private int patientId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private Integer doctorId;
    private String doctorName;
    private String doctorSpecialization;
    private Integer riskRating;
    private int totalRecords;


    public PatientDashboardDTO() {
    }

    public PatientDashboardDTO(
            int patientId,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            String gender,
            Integer doctorId,
            String doctorName,
            String doctorSpecialization,
            Integer riskRating,
            int totalRecords
            ) {

        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.doctorSpecialization = doctorSpecialization;
        this.riskRating = riskRating;
        this.totalRecords = totalRecords;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Integer getRiskRating() {
        return riskRating;
    }

    public void setRiskRating(Integer riskRating) {
        this.riskRating = riskRating;
    }
    public void setTotalRecords(int totalRecords) { this.totalRecords = totalRecords; }
    public int getTotalRecords() {return totalRecords;}

    public String getDoctorSpecialization() {
        return doctorSpecialization;
    }
    public void setDoctorSpecialization(String doctorSpecialization) {this.doctorSpecialization = doctorSpecialization;}
}