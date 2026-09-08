package com.shivam.ehip.medicalHistory;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shivam.ehip.medicalHistory.MedicalHistoryService;

import jakarta.validation.Valid;

import com.shivam.ehip.medicalHistory.MedicalHistoryRequestDTO;
import com.shivam.ehip.medicalHistory.MedicalHistory;

@RestController
@RequestMapping("/medical-history")
public class MedicalHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(MedicalHistoryController.class);

    private final MedicalHistoryService service;

    public MedicalHistoryController(MedicalHistoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<MedicalHistory> getAllMedicalHistory() {

        logger.info("Received request to fetch all medical history records");

        List<MedicalHistory> historyList = service.getAllMedicalHistory();

        logger.info("Successfully fetched {} medical history records", historyList != null ? historyList.size() : 0);

        return historyList;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addMedicalHistory(@Valid @RequestBody MedicalHistoryRequestDTO request) {

        logger.info("Received request to add medical history for patientId={}", request.getPatientId());

        service.addMedicalHistory(request);

        logger.info("Medical history added successfully for patientId={}", request.getPatientId());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Medical history added"));
    }

    @GetMapping("/{patientId}")
    public List<MedicalHistory> getHistoryByPatientid(@PathVariable int patientId) {

        logger.info("Received request to fetch medical history for patientId={}", patientId);

        List<MedicalHistory> historyList = service.getHistoryByPatientid(patientId);

        logger.info("Fetched {} medical history records for patientId={}", historyList != null ? historyList.size() : 0,
                patientId);

        return historyList;
    }
}
