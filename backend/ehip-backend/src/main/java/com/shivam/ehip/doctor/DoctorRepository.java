package com.shivam.ehip.doctor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.shivam.ehip.doctor.DoctorDashboardDTO;
import com.shivam.ehip.doctor.DoctorPatientDTO;
import com.shivam.ehip.doctor.Doctor;

@Repository
public class DoctorRepository {

    private static final Logger logger = LoggerFactory.getLogger(DoctorRepository.class);

    private static String errorMsg = "Error while updating patient";

    private final JdbcTemplate jdbcTemplate;

    public DoctorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ✅ Get all doctors
    public List<DoctorDashboardDTO> getAllDoctors() {

        logger.info("Executing stored procedure: GetDoctorDashboard");

        List<DoctorDashboardDTO> doctors = jdbcTemplate.query("EXEC GetDoctorDashboard", (rs, rowNum) -> {
            DoctorDashboardDTO dto = new DoctorDashboardDTO();

            dto.setDoctorId(rs.getInt("DoctorId"));
            dto.setDoctorName(rs.getString("DoctorName"));
            dto.setSpecialization(rs.getString("Specialization"));
            dto.setPatientCount(rs.getInt("PatientCount"));
            dto.setHighRiskPatientCount(rs.getInt("HighRiskPatientCount"));

            return dto;
        });

        logger.info("Fetched {} doctors from database", doctors.size());

        return doctors;
    }

    public List<DoctorDashboardDTO> getDoctorDashboard(int pageNumber, int pageSize, String searchTerm) {

        logger.info(
                "Executing GetDoctorDashboard for pageNumber={}, pageSize={}",
                pageNumber,
                pageSize
        );

        List<DoctorDashboardDTO> doctors = jdbcTemplate.query(
                "EXEC GetDoctorDashboard ?, ?, ?",
                (rs, rowNum) -> {

                    DoctorDashboardDTO dto = new DoctorDashboardDTO();

                    dto.setDoctorId(rs.getInt("DoctorId"));
                    dto.setDoctorName(rs.getString("DoctorName"));
                    dto.setSpecialization(rs.getString("Specialization"));
                    dto.setPatientCount(rs.getInt("PatientCount"));
                    dto.setHighRiskPatientCount(
                            rs.getInt("HighRiskPatientCount")
                    );
                    dto.setTotalRecords(
                            rs.getInt("TotalRecords")
                    );

                    return dto;
                },
                pageNumber,
                pageSize,
                searchTerm
        );

        logger.info(
                "Fetched {} doctors for dashboard",
                doctors.size()
        );

        return doctors;
    }

    // ✅ Get patients by doctor
    public List<DoctorPatientDTO> getPatientsByDoctor(int doctorId) {

        logger.info("Executing stored procedure: GetPatientsByDoctor for doctorId={}", doctorId);

        List<DoctorPatientDTO> patients = jdbcTemplate.query("EXEC GetPatientsByDoctor ?", (rs, rowNum) -> {
            DoctorPatientDTO dto = new DoctorPatientDTO();

            dto.setPatientId(rs.getInt("PatientId"));
            dto.setPatientName(rs.getString("PatientName"));
            dto.setRiskRating(rs.getInt("RiskRating"));

            return dto;
        }, doctorId);

        logger.info("Fetched {} patients for doctorId={}", patients.size(), doctorId);

        return patients;
    }

    public Doctor getDoctorById(int doctorId) {

        logger.info("Executing stored procedure: getDoctorById");

        List<Doctor> list = jdbcTemplate.query("EXEC getDoctorById ?",
                (rs, rowNum) -> new Doctor(rs.getInt("DoctorId"), rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Specialization"), rs.getString("Phone"), rs.getString("Email")),
                doctorId);

        return list.isEmpty() ? null : list.get(0);
    }

    public boolean addDoctor(Doctor doctor) {

        try {
            int rows = jdbcTemplate.update("EXEC AddDoctor ?, ?, ?, ?, ?", doctor.getFirstName(), doctor.getLastName(),
                    doctor.getSpecialization(), doctor.getPhone(), doctor.getEmail());

            return rows > 0;
        } catch (Exception e) {
            logger.error(errorMsg, e);
            return false;
        }

    }

    public boolean updateDoctor(int doctorId, Doctor doctor) {

        logger.info("Executing UpdateDoctor for id={}", doctorId);

        try {
            jdbcTemplate.update("EXEC UpdateDoctor ?,?,?,?,?,?", doctorId, doctor.getFirstName(),
                    doctor.getLastName(), doctor.getSpecialization(), doctor.getPhone(), doctor.getEmail());

            logger.info("UpdateDoctor executed for id={}, rows affected={}", doctorId);

            return true;
        } catch (Exception e) {
            logger.error(errorMsg, e);
            return false;
        }

    }

    public boolean deleteDoctor(int doctorId) {
        logger.info("Executing DeleteDoctor for id={}", doctorId);

        try {
            jdbcTemplate.update("EXEC DeleteDoctor ?", doctorId);

            logger.info("DeleteDoctor executed for id={}, rows affected={}", doctorId);

            return true;
        } catch (Exception e) {
            logger.error(errorMsg, e);
            return false;
        }

    }

    public List<DoctorSearchDTO> searchDoctors(String searchTerm) {

        logger.info(
                "Searching doctors with searchTerm={}",
                searchTerm
        );

        List<DoctorSearchDTO> doctors = jdbcTemplate.query(
                "EXEC SearchDoctors ?, ?",

                (rs, rowNum) -> new DoctorSearchDTO(

                        rs.getInt("DoctorId"),

                        rs.getString("DoctorName"),

                        rs.getString("Specialization")
                ),

                searchTerm == null ? "" : searchTerm,

                25
        );

        logger.info(
                "Found {} matching doctors",
                doctors.size()
        );

        return doctors;
    }

}
