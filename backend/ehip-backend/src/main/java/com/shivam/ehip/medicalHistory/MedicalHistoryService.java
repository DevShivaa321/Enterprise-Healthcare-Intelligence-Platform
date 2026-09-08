package com.shivam.ehip.medicalHistory;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import com.shivam.ehip.medicalHistory.MedicalHistoryRequestDTO;
import com.shivam.ehip.medicalHistory.MedicalHistory;
import com.shivam.ehip.medicalHistory.MedicalHistoryRepository;

@Service
public class MedicalHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(MedicalHistoryService.class);

    private final MedicalHistoryRepository medicalHistoryRepo;

    public MedicalHistoryService(MedicalHistoryRepository medicalHistoryRepo) {
        this.medicalHistoryRepo = medicalHistoryRepo;
    }

    public List<MedicalHistory> getAllMedicalHistory() {

        logger.info("Fetching all medical history records from repository");

        List<MedicalHistory> historyList = medicalHistoryRepo.getAllMedicalHistory();

        logger.debug("Total medical history records fetched: {}", historyList != null ? historyList.size() : 0);

        return historyList;
    }

    public void addMedicalHistory(MedicalHistoryRequestDTO req) {

        int patientId = req.getPatientId();

        logger.info("Adding medical history for patientId={}", patientId);

        // ✅ Validation check
        if (!medicalHistoryRepo.patientExists(patientId)) {

            logger.error("Validation failed: patientId={} does not exist", patientId);

            throw new IllegalArgumentException("Patient ID " + patientId + " does not exist");
        }

        medicalHistoryRepo.addMedicalHistory(req);

        logger.info("Medical history added successfully for patientId={}", patientId);
    }

    public List<MedicalHistory> getHistoryByPatientid(int patientId) {

        logger.info("Fetching medical history for patientId={}", patientId);

        List<MedicalHistory> historyList = medicalHistoryRepo.getByPatientId(patientId);

        logger.debug("Fetched {} medical history records for patientId={}",
                historyList != null ? historyList.size() : 0, patientId);

        return historyList;
    }
}
