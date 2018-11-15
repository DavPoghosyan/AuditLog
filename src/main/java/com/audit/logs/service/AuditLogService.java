package com.audit.logs.service;

import com.audit.logs.domain.AuditLog;
import com.audit.logs.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public Iterable<AuditLog> list() {
        return auditLogRepository.findAll();
    }

    public int getAlertsCount() {
        return auditLogRepository.findByIsAlertTrue().size();
    }

    public AuditLog save(AuditLog logs) {
        return auditLogRepository.save(logs);
    }

}