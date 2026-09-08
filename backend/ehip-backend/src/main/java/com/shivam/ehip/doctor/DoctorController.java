package com.shivam.ehip.doctor;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);

    private static String mssg="message";
    private static String mssgBody= "Doctor not found with id:";

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public List<DoctorDashboardDTO> getDoctors() {
        logger.info("Received request to fetch all doctors");

        List<DoctorDashboardDTO> doctors = doctorService.getAllDoctors();

        logger.info("Successfully fetched {} doctors", doctors != null ? doctors.size() : 0);

        return doctors;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<DoctorDashboardDTO>> getDoctorDashboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) {

        logger.info(
                "Received doctor dashboard request: page={}, size={}, search={}",
                page, size, search
        );

        return ResponseEntity.ok(
                doctorService.getDoctorDashboard(page, size, search)
        );
    }

    @GetMapping("/{doctorId}/patients")
    public List<DoctorPatientDTO> getDoctorPatients(@PathVariable int doctorId) {
        logger.info("Received request to fetch patients for doctorId={}", doctorId);

        List<DoctorPatientDTO> patients = doctorService.getDoctorPatients(doctorId);

        logger.info("Successfully fetched {} patients for doctorId={}",
                patients != null ? patients.size() : 0, doctorId);

        return patients;
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<?> getDoctorById(@PathVariable int doctorId) {

        logger.info("Received request to fetch doctor with id={}", doctorId);

        Doctor doctor = doctorService.getDoctorById(doctorId);

        if (doctor == null) {
            logger.warn("Doctor not found with id={}", doctorId);

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(mssg, mssgBody + doctorId));
        }

        logger.info("Successfully fetched doctor with id={}", doctorId);

        return ResponseEntity.ok(doctor);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addDoctor(@Valid @RequestBody Doctor doctor) {

        logger.info("Received request to add a new doctor");

        doctorService.addDoctor(doctor);

        logger.info("Doctor added successfully");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(mssg, "Doctor added successfully"));
    }


    @PutMapping("/{doctorId}")
    public ResponseEntity<Map<String, String>> updateDoctor(
            @PathVariable int doctorId,
            @RequestBody Doctor doctor) {

        logger.info("Received request to update doctor with id={}", doctorId);

        boolean isUpdated = doctorService.updateDoctor(doctorId, doctor);

        if (isUpdated) {
            logger.info("Successfully updated doctor with id={}", doctorId);

            return ResponseEntity.ok(
                    Map.of(mssg, "Doctor updated successfully")
            );
        } else {
            logger.warn("Doctor not found for update with id={}", doctorId);

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(mssg, mssgBody + doctorId));
        }
    }

    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Map<String, String>> deleteDoctor(@PathVariable int doctorId) {

        logger.info("Received request to delete doctor with id={}", doctorId);

        boolean isDeleted = doctorService.deleteDoctor(doctorId);

        if (isDeleted) {
            logger.info("Successfully deleted doctor with id={}", doctorId);

            return ResponseEntity.ok(
                    Map.of("message", "Doctor deleted successfully")
            );
        } else {
            logger.warn("Doctor not found with id={}", doctorId);

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(mssg, mssgBody + doctorId));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<DoctorSearchDTO>> searchDoctors(@RequestParam(defaultValue = "") String search) {

        logger.info("Received request to search doctors: search={}", search);

        return ResponseEntity.ok(
                doctorService.searchDoctors(search)
        );
    }

}
