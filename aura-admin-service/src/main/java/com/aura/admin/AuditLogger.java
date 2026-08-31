package com.aura.admin;

import com.aura.admin.entity.AuditLog;
import com.aura.admin.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Helper ghi Audit Log dung chung, goi truc tiep tu Controller (khong tach rieng Service layer
 * de giu kien truc don gian, it file).
 */
@Component
@RequiredArgsConstructor
public class AuditLogger {

    private final AuditLogRepository auditLogRepository;

    public void log(String action, String targetType, Long targetId, String detail) {
        Long adminId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        auditLogRepository.save(AuditLog.builder()
                .adminId(adminId)
                .action(action)
                .targetType(targetType)
                .targetId(targetId)
                .detail(detail)
                .build());
    }
}
