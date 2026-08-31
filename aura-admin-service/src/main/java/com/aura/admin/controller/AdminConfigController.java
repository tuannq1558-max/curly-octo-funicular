package com.aura.admin.controller;

import com.aura.admin.AuditLogger;
import com.aura.admin.dto.ApiResponse;
import com.aura.admin.entity.ServicePackage;
import com.aura.admin.entity.SystemConfig;
import com.aura.admin.repository.ServicePackageRepository;
import com.aura.admin.repository.SystemConfigRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * UC6: Cau hinh tham so AI (threshold, retraining policy...).
 * UC8: Quan ly goi dich vu.
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminConfigController {

    private final SystemConfigRepository systemConfigRepository;
    private final ServicePackageRepository servicePackageRepository;
    private final AuditLogger auditLogger;

    // ---------- System Config (UC6) ----------

    @GetMapping("/configs")
    public ApiResponse<List<SystemConfig>> getAllConfigs() {
        return ApiResponse.ok(systemConfigRepository.findAll());
    }

    public record ConfigRequest(@NotBlank String configKey, @NotBlank String configValue) {}

    @PutMapping("/configs")
    public ApiResponse<SystemConfig> upsertConfig(@Valid @RequestBody ConfigRequest req) {
        SystemConfig config = systemConfigRepository.findByConfigKey(req.configKey())
                .orElse(SystemConfig.builder().configKey(req.configKey()).version(0).build());

        config.setConfigValue(req.configValue());
        config.setVersion(config.getVersion() + 1);
        config.setUpdatedAt(LocalDateTime.now());
        SystemConfig saved = systemConfigRepository.save(config);

        auditLogger.log("UPDATE_CONFIG", "SYSTEM_CONFIG", saved.getId(),
                req.configKey() + " = " + req.configValue() + " (v" + saved.getVersion() + ")");

        return ApiResponse.ok("Cap nhat cau hinh thanh cong (khong downtime)", saved);
    }

    // ---------- Service Package (UC8) ----------

    @GetMapping("/packages")
    public ApiResponse<List<ServicePackage>> getActivePackages() {
        return ApiResponse.ok(servicePackageRepository.findByActiveTrue());
    }

    public record PackageRequest(
            @NotBlank String name,
            @NotNull @Min(1) Integer analysisCredits,
            @NotNull @DecimalMin("0.0") BigDecimal price,
            @NotNull @Min(1) Integer durationDays) {}

    @PostMapping("/packages")
    public ApiResponse<ServicePackage> createPackage(@Valid @RequestBody PackageRequest req) {
        ServicePackage pkg = ServicePackage.builder()
                .name(req.name())
                .analysisCredits(req.analysisCredits())
                .price(req.price())
                .durationDays(req.durationDays())
                .active(true)
                .build();
        ServicePackage saved = servicePackageRepository.save(pkg);

        auditLogger.log("CREATE_PACKAGE", "SERVICE_PACKAGE", saved.getId(), saved.getName());
        return ApiResponse.ok("Tao goi dich vu thanh cong", saved);
    }

    @PatchMapping("/packages/{id}/deactivate")
    public ApiResponse<Void> deactivatePackage(@PathVariable Long id) {
        ServicePackage pkg = servicePackageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay ServicePackage id = " + id));
        pkg.setActive(false);
        servicePackageRepository.save(pkg);

        auditLogger.log("DEACTIVATE_PACKAGE", "SERVICE_PACKAGE", id, null);
        return ApiResponse.ok("Da vo hieu hoa goi dich vu (khong xoa cung de giu lich su giao dich)", null);
    }
}
