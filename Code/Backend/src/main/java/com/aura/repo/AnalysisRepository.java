package com.aura.repo;

import com.aura.model.Analysis;
import com.aura.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalysisRepository extends JpaRepository<Analysis, Long> {

    List<Analysis> findByUserOrderByCreatedAtDesc(User user);
}