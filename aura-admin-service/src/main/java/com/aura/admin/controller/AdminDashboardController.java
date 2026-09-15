package com.aura.admin.controller;

import com.aura.admin.dto.ApiResponse;
import com.aura.admin.entity.Clinic;
import com.aura.admin.entity.Transaction;
import com.aura.admin.entity.User;
import com.aura.admin.repository.AuditLogRepository;
import com.aura.admin.repository.ClinicRepository;
import com.aura.admin.repository.TransactionRepository;
import com.aura.admin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * UC9: Billing - xem lich su giao dich toan he thong.
 * UC10, UC11: Dashboard tong hop & System Analytics.
 * UC12: Audit Log.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;
    private final TransactionRepository transactionRepository;
    private final AuditLogRepository auditLogRepository;

    // ---------- Billing (UC9) ----------

    @GetMapping("/transactions")
    public ApiResponse<List<Transaction>> getTransactions() {
        return ApiResponse.ok(transactionRepository.findAll());
    }

    // ---------- Dashboard & Analytics (UC10, UC11) ----------

    @GetMapping("/dashboard/stats")
    public ApiResponse<Map<String, Object>> getDashboardStats() {
        long totalUsers = userRepository.countByRole(User.Role.USER);
        long totalDoctors = userRepository.countByRole(User.Role.DOCTOR);
        long totalClinics = clinicRepository.count();
        long pendingClinics = clinicRepository.countByStatus(Clinic.Status.PENDING);

        BigDecimal revenue30d = transactionRepository.sumRevenueBetween(
                LocalDateTime.now().minusDays(30), LocalDateTime.now());

        Map<String, Object> stats = Map.of(
                "totalUsers", totalUsers,
                "totalDoctors", totalDoctors,
                "totalClinics", totalClinics,
                "pendingClinicApprovals", pendingClinics,
                "revenueLast30Days", revenue30d
        );

        return ApiResponse.ok(stats);
    }

    // ---------- Audit Log (UC12) ----------

    @GetMapping("/audit-logs")
    public ApiResponse<?> getAuditLogs(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int size) {
        var result = auditLogRepository.findAll(
                org.springframework.data.domain.PageRequest.of(page, size));
        return ApiResponse.ok(result);
    }
}
