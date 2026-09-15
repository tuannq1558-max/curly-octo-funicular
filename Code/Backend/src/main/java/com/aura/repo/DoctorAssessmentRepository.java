package com.aura.repo;

import com.aura.model.DoctorAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DoctorAssessmentRepository extends JpaRepository<DoctorAssessment, Long> {
    List<DoctorAssessment> findByImagePatientIdOrderByAssessedAtDesc(Long patientId);
    boolean existsByImageId(Long imageId);
    long countByRiskLevelIgnoreCase(String riskLevel);
}
