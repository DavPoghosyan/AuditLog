package com.audit.logs.controller;

import com.audit.logs.domain.AuditLog;
import com.audit.logs.service.AuditLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/logs")
public class AuditLogController {

    private AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping("/list")
    public Iterable<AuditLog> list() {
        return auditLogService.list();
    }
}
