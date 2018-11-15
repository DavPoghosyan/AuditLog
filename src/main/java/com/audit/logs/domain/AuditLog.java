package com.audit.logs.domain;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;
    private String eventId;
    private long eventDuration;
    private String type;
    private String host;
    private boolean isAlert;



}
