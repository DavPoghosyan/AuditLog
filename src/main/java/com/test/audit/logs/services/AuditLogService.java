package com.test.audit.logs.services;

import com.test.audit.logs.domain.AuditLog;
import com.test.audit.logs.repository.AuditLogRepository;
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

    public AuditLog save(AuditLog logs) {
        return auditLogRepository.save(logs);
    }

}