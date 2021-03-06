package com.audit.logs.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dpoghosyan on 11/15/18.
 */
@Configuration
public class ApplicationProperties {

    @Value( "${audit.logs.alert.threshold.ms}" )
    private long alertThresholdMs;

    @Value( "${audit.logs.file}" )
    private String logFile;

    public long getAlertThresholdMs() {
        return alertThresholdMs;
    }

    public String getLogFile() {
        return logFile;
    }
}
