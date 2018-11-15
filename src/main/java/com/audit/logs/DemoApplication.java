package com.audit.logs;

import com.audit.logs.domain.AuditLog;
import com.audit.logs.service.AuditLogService;
import com.audit.logs.utils.ApplicationProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.audit.logs.utils.LogsModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.OptionalLong;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@SpringBootApplication
public class DemoApplication {

    private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);


    @Autowired
    AuditLogService auditLogService;

    @Autowired
    ApplicationProperties applicationProperties;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            // read json and write to db
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<LogsModel>> typeReference = new TypeReference<List<LogsModel>>(){};
            InputStream inputStream = TypeReference.class.getResourceAsStream("/json/logs.json");
            try {
                List<LogsModel> logs = mapper.readValue(inputStream,typeReference);
                Map<String, List<LogsModel>> logsMap = logs.stream()
                        .collect(Collectors.groupingBy(LogsModel::getId));
                logsMap.forEach(getConsumer());
                logger.info(String.format("%s Rows of Logs have been saved!", logsMap.keySet().size()));
                logger.debug(String.format("%s Alerts have been detected in Logs", auditLogService.getAlertsCount()));
            } catch (IOException e){
                logger.error("Unable to save logs: " + e.getMessage());
            }
        };
    }

    private BiConsumer<String, List<LogsModel>> getConsumer() {
        return (String keyStr, List<LogsModel> logs) -> {
            AuditLog auditLog = new AuditLog();
            auditLog.setEventId(keyStr);
            auditLog.setHost(logs.get(0).getHost());
            auditLog.setType(logs.get(0).getType());
            OptionalLong optionalLong = OptionalLong.empty();
            if(logs.size() > 1) {
                optionalLong = OptionalLong.of(logs.get(1).getTimestamp());
            }
            long eventDuration = Math.abs(logs.get(0).getTimestamp() - optionalLong.orElse(Instant.now().getEpochSecond()));
            auditLog.setEventDuration(eventDuration);
            if(eventDuration > applicationProperties.getAlertThresholdMs()) {
                auditLog.setAlert(true);
            }
            auditLogService.save(auditLog);
        };
    }
}
