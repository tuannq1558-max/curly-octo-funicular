package com.aura.controller;

import com.aura.model.DoctorAssessment;
import com.aura.model.RetinalImage;
import com.aura.model.User;
import com.aura.repo.DoctorAssessmentRepository;
import com.aura.repo.RetinalImageRepository;
import com.aura.repo.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.aura.model.Role;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    private final RetinalImageRepository images;
    private final DoctorAssessmentRepository assessments;
    private final UserRepository users;

    public DoctorController(RetinalImageRepository images,
                            DoctorAssessmentRepository assessments,
                            UserRepository users) {
        this.images = images;
        this.assessments = assessments;
        this.users = users;
    }

    @GetMapping("/reviews/pending")
    public List<RetinalImage> pending() {
        return images.findByStatusOrderByUploadedAtAsc("WAITING_FOR_DOCTOR");
    }

    @PostMapping("/reviews/{imageId}")
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorAssessment assess(@PathVariable Long imageId,
                                   @RequestBody AssessmentRequest request) {
        RetinalImage image = images.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Image not found"));
        User doctor = users.findById(request.doctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        if (doctor.getRole() != Role.DOCTOR) {
            throw new IllegalArgumentException("Account is not a doctor");
        }
        if (assessments.existsByImageId(imageId)) {
            throw new IllegalArgumentException("This image has already been assessed");
        }

        DoctorAssessment assessment = new DoctorAssessment();
        assessment.setImage(image);
        assessment.setDoctor(doctor);
        assessment.setRiskLevel(request.riskLevel());
        assessment.setFinding(request.finding());
        assessment.setDiagnosis(request.diagnosis());
        assessment.setRecommendation(request.recommendation());
        assessment.setNote(request.note());
        image.setStatus("COMPLETED");
        images.save(image);
        return assessments.save(assessment);
    }

    public record AssessmentRequest(
            Long doctorId,
            String riskLevel,
            String finding,
            String diagnosis,
            String recommendation,
            String note
    ) {}
}
