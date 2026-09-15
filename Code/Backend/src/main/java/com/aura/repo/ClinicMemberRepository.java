package com.aura.repo;

import com.aura.model.ClinicMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClinicMemberRepository extends JpaRepository<ClinicMember, Long> {
    List<ClinicMember> findByClinicIdOrderByRoleAscFullNameAsc(Long clinicId);
    long countByClinicIdAndRole(Long clinicId, String role);
}
