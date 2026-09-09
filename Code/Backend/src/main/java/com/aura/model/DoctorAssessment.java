package com.aura.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_assessments")
public class DoctorAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private RetinalImage image;

    @ManyToOne(optional = false)
    private User doctor;

    @Column(nullable = false, length = 20)
    private String riskLevel;

    @Column(length = 2000)
    private String finding;

    @Column(length = 2000)
    private String diagnosis;

    @Column(length = 2000)
    private String recommendation;

    @Column(length = 2000)
    private String note;

    private LocalDateTime assessedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public RetinalImage getImage() { return image; }
    public void setImage(RetinalImage image) { this.image = image; }
    public User getDoctor() { return doctor; }
    public void setDoctor(User doctor) { this.doctor = doctor; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public String getFinding() { return finding; }
    public void setFinding(String finding) { this.finding = finding; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public LocalDateTime getAssessedAt() { return assessedAt; }
}
