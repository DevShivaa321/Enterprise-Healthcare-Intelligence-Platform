package com.shivam.ehip.doctor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class Doctor {

    private Integer doctorId;

    @NotBlank
    private String firstName;

    private String lastName;

    @NotBlank
    private String specialization;


    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[^0-9].*", message = "Email should not start with a number")
    private String email;

    @Pattern(
            regexp="^[1-9]\\d{9}$",
            message="Invalid Phone Number")
    private String phone;

    public Doctor() {}

    public Doctor(Integer doctorId, String firstName, String lastName, String specialization, String phone, String email) {
        this.doctorId=doctorId;
        this.firstName=firstName;
        this.lastName=lastName;
        this.specialization=specialization;
        this.phone=phone;
        this.email=email;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
