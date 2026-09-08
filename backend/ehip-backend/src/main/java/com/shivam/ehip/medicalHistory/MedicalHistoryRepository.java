package com.shivam.ehip.medicalHistory;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.shivam.ehip.medicalHistory.MedicalHistoryRequestDTO;
import com.shivam.ehip.medicalHistory.MedicalHistory;

@Repository
public class MedicalHistoryRepository {

    private static final Logger logger = LoggerFactory.getLogger(MedicalHistoryRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public MedicalHistoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ✅ getAllMedicalHistory
    public List<MedicalHistory> getAllMedicalHistory() {

        logger.info("Executing stored procedure: getMedicalHistory");

        List<MedicalHistory> list = jdbcTemplate.query("EXEC getMedicalHistory",
                (rs, rowNum) -> new MedicalHistory(rs.getInt("HistoryId"), rs.getInt("PatientId"),
                        rs.getDouble("SugarLevel"), rs.getBoolean("HasDiabetes"), rs.getInt("BloodPressureSys"),
                        rs.getInt("BloodPressureDia"), rs.getInt("HeartRate"), rs.getInt("Cholesterol"),
                        rs.getDouble("BMI"), rs.getBoolean("Smoking"), rs.getBoolean("AlcoholConsumption"),
                        rs.getDate("RecordedAt").toLocalDate()));

        logger.info("Fetched {} medical history records", list.size());

        return list;
    }

    // ✅ addMedicalHistory
    public boolean addMedicalHistory(MedicalHistoryRequestDTO req) {

        int patientId = req.getPatientId();

        logger.info("Executing addMedicalHistories for patientId={}", patientId);

        if (req.getBloodPressureDia() > req.getBloodPressureSys()) {
            logger.error("Invalid BP values for patientId={} (Dia > Sys)", patientId);
            throw new IllegalArgumentException("Diastolic BP cannot exceed Systolic BP");
        }

        int rows = jdbcTemplate.update("EXEC addMedicalHistories ?,?,?,?,?,?,?,?,?,?", req.getPatientId(),
                req.getSugarLevel(), req.isHasDiabetes(), req.getBloodPressureSys(), req.getBloodPressureDia(),
                req.getHeartRate(), req.getCholesterol(), req.getBmi(), req.isSmoking(), req.isAlcoholConsumption());

        logger.info("addMedicalHistories executed for patientId={}, rows affected={}", patientId, rows);

        return rows > 0;
    }

    // ✅ patientExists
    public boolean patientExists(int patientId) {

        logger.debug("Checking existence of patientId={}", patientId);

        String sql = "SELECT COUNT(*) FROM Patient WHERE PatientId = ?";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, patientId);

        boolean exists = count != null && count > 0;

        logger.debug("Patient existence for id={} is {}", patientId, exists);

        return exists;
    }

    // ✅ getByPatientId
    public List<MedicalHistory> getByPatientId(int patientId) {

        logger.info("Executing getMedicalHistoryByPatientId for patientId={}", patientId);

        List<MedicalHistory> list = jdbcTemplate.query("EXEC getMedicalHistoryByPatientId ?",
                (rs, rowNum) -> new MedicalHistory(rs.getInt("HistoryId"), rs.getInt("PatientId"),
                        rs.getDouble("SugarLevel"), rs.getBoolean("HasDiabetes"), rs.getInt("BloodPressureSys"),
                        rs.getInt("BloodPressureDia"), rs.getInt("HeartRate"), rs.getInt("Cholesterol"),
                        rs.getDouble("BMI"), rs.getBoolean("Smoking"), rs.getBoolean("AlcoholConsumption"),
                        rs.getDate("RecordedAt").toLocalDate()),
                patientId);

        logger.info("Fetched {} records for patientId={}", list.size(), patientId);

        return list;
    }
}
