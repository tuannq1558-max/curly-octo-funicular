package com.aura.controller;

import com.aura.model.Clinic;
import com.aura.model.ClinicMember;
import com.aura.repo.ClinicMemberRepository;
import com.aura.repo.ClinicRepository;
import com.aura.repo.DoctorAssessmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clinic")
public class ClinicController {
    private final ClinicRepository clinics;
    private final ClinicMemberRepository members;
    private final DoctorAssessmentRepository assessments;

    public ClinicController(ClinicRepository clinics, ClinicMemberRepository members, DoctorAssessmentRepository assessments) {
        this.clinics = clinics;
        this.members = members;
        this.assessments = assessments;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard(@RequestParam(defaultValue = "1") Long clinicId) {
        Clinic clinic = clinics.findById(clinicId)
                .orElseThrow(() -> new IllegalArgumentException("Clinic not found: " + clinicId));
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("clinic", clinic);
        result.put("totalDoctors", members.countByClinicIdAndRole(clinicId, "DOCTOR"));
        result.put("totalPatients", members.countByClinicIdAndRole(clinicId, "PATIENT"));
        result.put("activeMembers", members.findByClinicIdOrderByRoleAscFullNameAsc(clinicId).stream()
                .filter(m -> "ACTIVE".equalsIgnoreCase(m.getStatus())).count());
        long highRisk = assessments.countByRiskLevelIgnoreCase("HIGH");
        result.put("highRiskPatients", highRisk);
        result.put("imagesThisMonth", assessments.count());
        return result;
    }

    @GetMapping
    public List<Clinic> list() { return clinics.findAll(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Clinic create(@RequestBody Clinic clinic) { return clinics.save(clinic); }

    @PutMapping("/{id}")
    public Clinic update(@PathVariable Long id, @RequestBody Clinic request) {
        Clinic clinic = clinics.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clinic not found: " + id));
        clinic.setName(request.getName());
        clinic.setAddress(request.getAddress());
        clinic.setLicenseNumber(request.getLicenseNumber());
        return clinics.save(clinic);
    }

    @GetMapping("/{id}/members")
    public List<ClinicMember> members(@PathVariable Long id) {
        if (!clinics.existsById(id)) throw new IllegalArgumentException("Clinic not found: " + id);
        return this.members.findByClinicIdOrderByRoleAscFullNameAsc(id);
    }

    @PostMapping("/{id}/members")
    @ResponseStatus(HttpStatus.CREATED)
    public ClinicMember addMember(@PathVariable Long id, @RequestBody ClinicMember member) {
        if (!clinics.existsById(id)) throw new IllegalArgumentException("Clinic not found: " + id);
        member.setClinicId(id);
        return members.save(member);
    }

    @PatchMapping("/members/{memberId}/status")
    public ClinicMember updateMemberStatus(@PathVariable Long memberId, @RequestParam String status) {
        ClinicMember member = members.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
        member.setStatus(status.toUpperCase());
        return members.save(member);
    }
}
