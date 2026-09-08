package com.shivam.ehip.risk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.shivam.ehip.risk.RiskResponseDTO;
import com.shivam.ehip.risk.RiskRepository;

@Service
public class RiskService {

    private static final Logger logger = LoggerFactory.getLogger(RiskService.class);

    private final RiskRepository riskRepo;

    public RiskService(RiskRepository riskRepo) {
        this.riskRepo = riskRepo;
    }

    public RiskResponseDTO getRisk(int patientId) {

        logger.info("Calculating risk for patientId={}", patientId);

        RiskResponseDTO risk = riskRepo.calculateRisk(patientId);

        if (risk != null) {
            logger.debug("Risk calculated: patientId={}, riskRating={}", risk.getPatientId(), risk.getRiskRating());
        } else {
            logger.warn("Risk data not found for patientId={}", patientId);
        }

        return risk;
    }
}
