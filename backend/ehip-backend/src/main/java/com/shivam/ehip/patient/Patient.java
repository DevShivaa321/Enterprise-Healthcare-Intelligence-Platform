package com.shivam.ehip.patient;

import java.time.LocalDate;
import jakarta.validation.constraints.*;

public class Patient {

    private int patientId;
    @NotBlank(message = "First name is required")
    @Size(min=2, message = "First name must be atleast two characters")
    @Pattern(regexp = "^[A-Za-z\\s'-]+$", message = "Name should contain only letters, spaces, hyphen, or apostrophe")
    private String firstName;

    @Pattern(regexp = "^[A-Za-z\\s'-]+$", message = "Name should contain only letters, spaces, hyphen, or apostrophe")
    private String lastName;

    @NotNull(message = "DOB is required")
    @PastOrPresent(message="Date of Birth cannot be in the future")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotNull(message = "Doctor is required")
    private Integer doctorId;

    private String doctorName;

    //private Integer riskRating;

    public Patient() {

    }

    //DEFAULT CONSTRUCTOR--> VERY IMPORTANT FOR POST
    // PARAMETERIZED CONTRUCTOR
    public Patient(int patientId, String firstName, String lastName, LocalDate dateOfBirth, String gender,
                   Integer doctorId, String doctorName) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        //this.riskRating = riskRating;
    }

    // GETTERS
    public int getPatientId() {
        return patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    // SETTER --> VERY IMP FOR @RequestBody
    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

//    public void setRiskRating(Integer riskRating) {
//        this.riskRating = riskRating;
//    }
//    public Integer getRiskRating() { return  riskRating; }

}
