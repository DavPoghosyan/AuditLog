package com.audit.logs.repository;

import com.audit.logs.domain.AuditLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuditLogRepository extends CrudRepository<AuditLog, Long> {


    @Query
    List<AuditLog> findByIsAlertTrue();

}
