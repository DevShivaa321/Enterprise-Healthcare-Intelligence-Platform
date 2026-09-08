package com.shivam.ehip.risk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shivam.ehip.risk.RiskResponseDTO;
import com.shivam.ehip.risk.RiskService;

@RestController
@RequestMapping("/risk")
public class RiskController {

    private static final Logger logger = LoggerFactory.getLogger(RiskController.class);

    private final RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    @GetMapping("/{patientId}")
    public RiskResponseDTO getRisk(@PathVariable int patientId) {

        logger.info("Received request to calculate risk for patientId={}", patientId);

        RiskResponseDTO risk = riskService.getRisk(patientId);

        // Logging only key value (not full map)
        if (risk != null) {
            logger.debug("Risk calculated: patientId={}, riskRating={}",
                    risk.getPatientId(),
                    risk.getRiskRating());
        } else {
            logger.warn("Risk data not found for patientId={}", patientId);
        }

        return risk;
    }
}
