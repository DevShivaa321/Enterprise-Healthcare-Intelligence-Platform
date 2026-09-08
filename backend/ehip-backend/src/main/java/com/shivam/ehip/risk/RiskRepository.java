package com.shivam.ehip.risk;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.shivam.ehip.risk.RiskResponseDTO;

@Repository
public class RiskRepository {

    private static final Logger logger = LoggerFactory.getLogger(RiskRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public RiskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public RiskResponseDTO calculateRisk(int patientId) {

        logger.info("Executing stored procedure: calculatePatientRisk for patientId={}", patientId);

        List<Map<String, Object>> list = jdbcTemplate.queryForList("EXEC calculatePatientRisk ?", patientId);
// queryForList---> returns Object, hence need to cast it
        if (!list.isEmpty()) {

            Map<String, Object> row = list.get(0);

            int riskRating = ((Number) row.get("RiskRating")).intValue();

            RiskResponseDTO dto = new RiskResponseDTO();
            dto.setPatientId(patientId);
            dto.setRiskRating(riskRating);

            logger.info("Risk calculated for patientId={}, riskRating={}", patientId, riskRating);

            return dto;

        } else {
            logger.warn("No risk data returned from DB for patientId={}", patientId);
        }

        return null;
    }
}
