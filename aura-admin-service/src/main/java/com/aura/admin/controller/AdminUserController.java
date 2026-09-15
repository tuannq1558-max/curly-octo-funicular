package com.aura.admin.controller;

import com.aura.admin.AuditLogger;
import com.aura.admin.dto.ApiResponse;
import com.aura.admin.entity.Clinic;
import com.aura.admin.entity.User;
import com.aura.admin.repository.ClinicRepository;
import com.aura.admin.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * UC1, UC2: Quan ly tai khoan User/Doctor.
 * UC3, UC4: Quan ly & Duyet/Tu choi dang ky Clinic.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;
    private final AuditLogger auditLogger;

    // ---------- User / Doctor (UC1, UC2) ----------

    @GetMapping("/users")
    public ApiResponse<List<User>> getUsersByRole(@RequestParam(defaultValue = "USER") User.Role role) {
        return ApiResponse.ok(userRepository.findByRole(role));
    }

    @PatchMapping("/users/{id}/status")
    public ApiResponse<User> updateUserStatus(@PathVariable Long id, @RequestParam User.Status status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay User id = " + id));

        user.setStatus(status);
        User saved = userRepository.save(user);

        auditLogger.log(status == User.Status.DISABLED ? "DISABLE_USER" : "ENABLE_USER",
                "USER", id, "Status -> " + status);

        return ApiResponse.ok("Cap nhat trang thai thanh cong", saved);
    }

    // ---------- Clinic (UC3, UC4) ----------

    @GetMapping("/clinics")
    public ApiResponse<List<Clinic>> getClinicsByStatus(@RequestParam(defaultValue = "PENDING") Clinic.Status status) {
        return ApiResponse.ok(clinicRepository.findByStatus(status));
    }

    public record ClinicReviewRequest(@NotNull Boolean approve, String rejectionReason) {}

    @PostMapping("/clinics/{id}/review")
    public ApiResponse<Clinic> reviewClinic(@PathVariable Long id, @RequestBody ClinicReviewRequest req) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay Clinic id = " + id));

        if (clinic.getStatus() != Clinic.Status.PENDING) {
            throw new IllegalArgumentException("Clinic da duoc xu ly truoc do: " + clinic.getStatus());
        }

        if (Boolean.TRUE.equals(req.approve())) {
            clinic.setStatus(Clinic.Status.APPROVED);
        } else {
            if (req.rejectionReason() == null || req.rejectionReason().isBlank()) {
                throw new IllegalArgumentException("Phai cung cap ly do khi tu choi Clinic");
            }
            clinic.setStatus(Clinic.Status.REJECTED);
            clinic.setRejectionReason(req.rejectionReason());
        }

        Clinic saved = clinicRepository.save(clinic);

        auditLogger.log(req.approve() ? "APPROVE_CLINIC" : "REJECT_CLINIC", "CLINIC", id,
                req.approve() ? "Approved" : "Rejected: " + req.rejectionReason());

        return ApiResponse.ok(saved);
    }

    @PostMapping("/clinics/{id}/suspend")
    public ApiResponse<Clinic> suspendClinic(@PathVariable Long id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay Clinic id = " + id));
        clinic.setStatus(Clinic.Status.SUSPENDED);
        Clinic saved = clinicRepository.save(clinic);

        auditLogger.log("SUSPEND_CLINIC", "CLINIC", id, null);
        return ApiResponse.ok(saved);
    }
}
