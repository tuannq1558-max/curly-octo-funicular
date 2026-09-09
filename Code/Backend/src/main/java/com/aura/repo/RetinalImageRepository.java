package com.aura.repo;

import com.aura.model.RetinalImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RetinalImageRepository extends JpaRepository<RetinalImage, Long> {
    List<RetinalImage> findByPatientIdOrderByUploadedAtDesc(Long patientId);
    List<RetinalImage> findByStatusOrderByUploadedAtAsc(String status);
}
