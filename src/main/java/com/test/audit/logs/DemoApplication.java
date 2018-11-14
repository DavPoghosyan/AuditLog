package com.test.audit.logs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.audit.logs.domain.AuditLog;
import com.test.audit.logs.services.AuditLogService;
import com.test.audit.logs.utils.LogsModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableJpaRepositories("com.test.audit.logs.repository")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    AuditLogService auditLogService;

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
                System.out.println(auditLogService.list());
                System.out.println(logs);
                System.out.println("Logs Saved!");
            } catch (IOException e){
                System.out.println("Unable to save logs: " + e.getMessage());
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
            if(eventDuration > 4) { // make configurable
                auditLog.setAlert(true);
            }
            auditLogService.save(auditLog);
        };
    }
}
