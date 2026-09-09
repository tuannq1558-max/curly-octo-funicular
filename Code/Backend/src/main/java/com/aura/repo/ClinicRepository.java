package com.aura.repo;

import com.aura.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    long countByStatus(String status);
}
