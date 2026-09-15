package com.aura.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Analysis {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private String filename;

 private String riskLevel;

 private Double riskScore;

 private String modelVersion;

 @Column(length = 5000)
 private String findings;

 private LocalDateTime createdAt = LocalDateTime.now();

 @ManyToOne(fetch = FetchType.LAZY)
 @JoinColumn(name = "user_id", nullable = false)
 private User user;

 public Long getId() {
  return id;
 }

 public String getFilename() {
  return filename;
 }

 public void setFilename(String filename) {
  this.filename = filename;
 }

 public String getRiskLevel() {
  return riskLevel;
 }

 public void setRiskLevel(String riskLevel) {
  this.riskLevel = riskLevel;
 }

 public Double getRiskScore() {
  return riskScore;
 }

 public void setRiskScore(Double riskScore) {
  this.riskScore = riskScore;
 }

 public String getModelVersion() {
  return modelVersion;
 }

 public void setModelVersion(String modelVersion) {
  this.modelVersion = modelVersion;
 }

 public String getFindings() {
  return findings;
 }

 public void setFindings(String findings) {
  this.findings = findings;
 }

 public LocalDateTime getCreatedAt() {
  return createdAt;
 }

 public User getUser() {
  return user;
 }

 public void setUser(User user) {
  this.user = user;
 }
}