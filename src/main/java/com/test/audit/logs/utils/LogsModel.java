package com.test.audit.logs.utils;

import com.fasterxml.jackson.databind.JsonSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogsModel {
    private String id;
    private String state;
    private String type;
    private String host;
    private Long timestamp;
}
