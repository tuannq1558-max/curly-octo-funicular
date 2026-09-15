package com.aura.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/** UC8: Quan ly goi dich vu. Khong xoa cung - chi deactivate. */
@Entity
@Table(name = "service_packages")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServicePackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "analysis_credits", nullable = false)
    private Integer analysisCredits;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
