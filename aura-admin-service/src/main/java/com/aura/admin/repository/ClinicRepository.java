package com.aura.admin.repository;

import com.aura.admin.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    List<Clinic> findByStatus(Clinic.Status status);
    long countByStatus(Clinic.Status status);
}
