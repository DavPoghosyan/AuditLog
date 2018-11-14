package com.test.audit.logs.repository;

import com.test.audit.logs.domain.AuditLog;
import org.springframework.data.repository.CrudRepository;

public interface AuditLogRepository extends CrudRepository<AuditLog, Long> {


}
