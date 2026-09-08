package com.shivam.ehip.patient;

import com.shivam.ehip.patient.PatientService;
import jakarta.validation.Valid;

import com.shivam.ehip.patient.Patient;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    private static String mssg= "message";

    @GetMapping
    public List<Patient> getPatients() {

        logger.info("Received request to fetch all patients");

        List<Patient> patients = patientService.getAllPatient();

        logger.info("Successfully fetched {} patients", patients != null ? patients.size() : 0);

        return patients;
    }

    @GetMapping("/dashboard")
    public List<PatientDashboardDTO> getPatientDashboard( @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size, @RequestParam(required = false) String search) {

        return patientService.getPatientDashboard(page, size, search);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable int id) {

        logger.info("Received request to fetch patient with id={}", id);

        Patient patient = patientService.getPatientById(id);

        if (patient == null) {
            logger.warn("Patient not found with id={}", id);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(mssg, "Patient not found with id: " + id));
        }

        logger.info("Successfully fetched patient with id={}", id);

        return ResponseEntity.ok(patient);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addPatient(@Valid @RequestBody Patient patient) {

        logger.info("Received request to add a new patient");

        patientService.addPatient(patient);

        logger.info("Patient added successfully");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(mssg, "Patient added successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updatePatient(@PathVariable int id, @RequestBody Patient patient) {

        logger.info("Received request to update patient with id={}", id);

        boolean isUpdated = patientService.updatePatient(id, patient);

        if (isUpdated) {
            logger.info("Successfully updated patient with id={}", id);

            return ResponseEntity.ok(Map.of(mssg, "Patient updated successfully"));
        } else {
            logger.warn("Patient not found for update with id={}", id);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(mssg, "Patient Not found with id: " + id));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePatient(@PathVariable int id) {

        logger.info("Received request to delete patient with id={}", id);

        boolean isDeleted = patientService.deletePatient(id);

        if (isDeleted) {
            logger.info("Successfully deleted patient with id={}", id);

            return ResponseEntity.ok(
                    Map.of(mssg, "Patient deleted successfully")
            );
        } else {
            logger.warn("Patient not found for deletion with id={}", id);

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(mssg, "Patient not found with id: " + id));
        }
    }

}
