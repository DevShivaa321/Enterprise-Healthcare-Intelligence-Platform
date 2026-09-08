package com.shivam.ehip.doctor;

import java.util.List;

import com.shivam.ehip.risk.RiskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.shivam.ehip.risk.RiskResponseDTO;

@Service
public class DoctorService {

    private static final Logger logger = LoggerFactory.getLogger(DoctorService.class);

    private final DoctorRepository doctorRepository;

    private final RiskService riskService;

    public DoctorService(DoctorRepository doctorRepository, RiskService riskService) {
        this.doctorRepository = doctorRepository;
        this.riskService = riskService;
    }

    public List<DoctorDashboardDTO> getAllDoctors() {

        logger.info("Fetching all doctors from repository dashbaord");
        return doctorRepository.getAllDoctors();
    }

    public List<DoctorDashboardDTO> getDoctorDashboard(int pageNumber, int pageSize, String search) {
        logger.info(
                "Fetching doctor dashboard: page={}, size={}, search={}",
                pageNumber, pageSize, search
        );
        return doctorRepository.getDoctorDashboard(pageNumber, pageSize, search);
    }

    public List<DoctorPatientDTO> getDoctorPatients(int doctorId) {

        logger.info("Fetching patients for doctorId={}", doctorId);

        List<DoctorPatientDTO> patients = doctorRepository.getPatientsByDoctor(doctorId);

        logger.debug("Fetched {} patients for doctorId={}", patients != null ? patients.size() : 0, doctorId);

        // RISK CALCULATION LOGIC FOR EACH PATIENT
        for (DoctorPatientDTO patient : patients) {

            RiskResponseDTO risk = riskService.getRisk(patient.getPatientId());

            int riskRating = risk.getRiskRating();

            logger.debug("Setting riskRating={} for patientId={}", riskRating, patient.getPatientId());

            patient.setRiskRating(riskRating);
        }

        logger.info("Returning patients list for doctorId={}", doctorId);

        return patients;
    }

    public Doctor getDoctorById(int doctorId) {

        logger.info("Fetching doctor with id={}", doctorId);

        Doctor doctor = doctorRepository.getDoctorById(doctorId);

        if (doctor == null) {
            logger.warn("Doctor not found in service layer for id={}", doctorId);
        } else {
            logger.debug("Doctor found for id={}", doctorId);
        }
        return doctor;
    }

    public boolean addDoctor(Doctor doctor) {

        logger.info("Adding new doctor");

        boolean result = doctorRepository.addDoctor(doctor);

        if (result) {
            logger.info("Doctor added successfully");
        } else {
            logger.warn("Failed to add doctor");
        }

        return result;
    }

    public boolean updateDoctor(int doctorId, Doctor doctor) {

        logger.info("Updating doctor with id={}", doctorId);

        boolean result = doctorRepository.updateDoctor(doctorId, doctor);

        if (result) {
            logger.info("Doctor updated successfully for id={}", doctorId);
        } else {
            logger.warn("Doctor update failed or not found for id={}", doctorId);
        }

        return result;
    }

    public boolean deleteDoctor(int doctorId) {

        logger.info("Deleting doctor with id={}", doctorId);

        boolean result = doctorRepository.deleteDoctor(doctorId);

        if (result) {
            logger.info("Doctor deleted successfully for id={}", doctorId);
        } else {
            logger.warn("Doctor deletion failed or not found for id={}", doctorId);
        }

        return result;
    }

    public List<DoctorSearchDTO> searchDoctors(String searchTerm) {

        logger.info("Searching doctors with term={}", searchTerm);

        return doctorRepository.searchDoctors(searchTerm);
    }
}
