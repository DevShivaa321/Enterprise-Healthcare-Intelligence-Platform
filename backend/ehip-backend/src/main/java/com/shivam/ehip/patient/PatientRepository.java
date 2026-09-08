package com.shivam.ehip.patient;

import java.sql.*;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.shivam.ehip.patient.Patient;

@Repository
public class PatientRepository {

    private static final Logger logger = LoggerFactory.getLogger(PatientRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public PatientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static String errorMsg = "Error while updating patient";

    public List<Patient> getAllPatient() {

        logger.info("Executing stored procedure: getAllPatient");

        return jdbcTemplate.query("EXEC getAllPatient",
                (rs, rowNum) -> new Patient(rs.getInt("PatientId"), rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getDate("DateOfBirth").toLocalDate(), rs.getString("Gender"), rs.getInt("DoctorId"),
                        rs.getString("DoctorName")));
    }

    public List<PatientDashboardDTO> getPatientDashboard(int pageNumber, int pageSize, String searchTerm) {

        logger.info(
                "Executing GetPatientDashboard for pageNumber={}, pageSize={}, searchTerm{}",
                pageNumber,
                pageSize,
                searchTerm
        );

        return jdbcTemplate.query(
                "EXEC GetPatientDashboard ?, ?, ?",
                (rs, rowNum) -> new PatientDashboardDTO(
                        rs.getInt("PatientId"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getDate("DateOfBirth").toLocalDate(),
                        rs.getString("Gender"),
                        rs.getObject("DoctorId") != null
                                ? rs.getInt("DoctorId")
                                : null,
                        rs.getString("DoctorName"),
                        rs.getString("DoctorSpecialization"),
                        rs.getObject("RiskRating") != null
                                ? rs.getInt("RiskRating")
                                : null,
                        rs.getInt("TotalRecords")
                ),
                pageNumber,
                pageSize,
                searchTerm == null ? "" : searchTerm
        );
    }

    public Patient getPatientById(int id) {

        logger.info("Executing stored procedure: getPatientById");

        List<Patient> list = jdbcTemplate.query("EXEC getPatientById ?",
                (rs, rowNum) -> new Patient(rs.getInt("PatientId"), rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getDate("DateOfBirth").toLocalDate(), rs.getString("Gender"), rs.getInt("DoctorId"),
                        rs.getString("DoctorName")),
                id);

        return list.isEmpty() ? null : list.get(0);
    }

    public boolean addPatient(Patient patient) {

        try {
            int rows = jdbcTemplate.update("EXEC AddPatient ?, ?, ?, ?, ?", patient.getFirstName(),
                    patient.getLastName(), Date.valueOf(patient.getDateOfBirth()), patient.getGender(),
                    patient.getDoctorId());

            return rows > 0;
        } catch (Exception e) {
            logger.error(errorMsg, e);
            return false;
        }

    }

    public boolean updatePatient(int id, Patient patient) {

        logger.info("Executing UpdatePatient for id={}", id);

        try {
            jdbcTemplate.update("EXEC UpdatePatient ?,?,?,?,?,?", id, patient.getFirstName(),
                    patient.getLastName(), Date.valueOf(patient.getDateOfBirth()), patient.getGender(),
                    patient.getDoctorId());

            logger.info("UpdatePatient executed for id={}, rows affected={}", id);

            return true;
        } catch (Exception e) {
            logger.error(errorMsg, e);
            return false;
        }

    }

    public boolean deletePatient(int id) {
        logger.info("Executing DeletePatient for id={}", id);

        try {
            jdbcTemplate.update("EXEC DeletePatient ?", id);

            logger.info("DeletePatient executed for id={}, rows affected={}", id);

            return true;
        } catch (Exception e) {
            logger.error(errorMsg, e);
            return false;
        }

    }

}