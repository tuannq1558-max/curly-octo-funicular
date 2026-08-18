package com.aura.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class Analysis {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String filename;
 private String riskLevel;
 private Double riskScore;
 private String modelVersion;
 private String findings;
 private LocalDateTime createdAt = LocalDateTime.now();
 public Long getId(){return id;} public String getFilename(){return filename;}
 public void setFilename(String v){filename=v;} public String getRiskLevel(){return riskLevel;}
 public void setRiskLevel(String v){riskLevel=v;} public Double getRiskScore(){return riskScore;}
 public void setRiskScore(Double v){riskScore=v;} public String getModelVersion(){return modelVersion;}
 public void setModelVersion(String v){modelVersion=v;} public String getFindings(){return findings;}
 public void setFindings(String v){findings=v;} public LocalDateTime getCreatedAt(){return createdAt;}
}