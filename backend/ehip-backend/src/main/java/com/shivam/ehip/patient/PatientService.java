package com.shivam.ehip.patient;

import com.shivam.ehip.patient.Patient;
import com.shivam.ehip.patient.PatientRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private static final Logger logger = LoggerFactory.getLogger(PatientService.class);

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getAllPatient() {

        logger.info("Fetching all patients from repository");

        List<Patient> patients = patientRepository.getAllPatient();

        logger.debug("Total patients fetched: {}", patients != null ? patients.size() : 0);

        return patients;
    }

    public List<PatientDashboardDTO> getPatientDashboard( int pageNumber, int pageSize, String searchTerm) {
        return patientRepository.getPatientDashboard(pageNumber, pageSize, searchTerm);
    }

    public Patient getPatientById(int id) {

        logger.info("Fetching patient with id={}", id);

        Patient patient = patientRepository.getPatientById(id);

        if (patient == null) {
            logger.warn("Patient not found in service layer for id={}", id);
        } else {
            logger.debug("Patient found for id={}", id);
        }

        return patient;
    }

    public boolean addPatient(Patient patient) {

        logger.info("Adding new patient");

        boolean result = patientRepository.addPatient(patient);

        if (result) {
            logger.info("Patient added successfully");
        } else {
            logger.warn("Failed to add patient");
        }

        return result;
    }

    public boolean updatePatient(int id, Patient patient) {

        logger.info("Updating patient with id={}", id);

        boolean result = patientRepository.updatePatient(id, patient);

        if (result) {
            logger.info("Patient updated successfully for id={}", id);
        } else {
            logger.warn("Patient update failed or not found for id={}", id);
        }

        return result;
    }

    public boolean deletePatient(int id) {

        logger.info("Deleting patient with id={}", id);

        boolean result = patientRepository.deletePatient(id);

        if (result) {
            logger.info("Patient deleted successfully for id={}", id);
        } else {
            logger.warn("Patient deletion failed or not found for id={}", id);
        }

        return result;
    }
}
