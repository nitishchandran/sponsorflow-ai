package com.nitish.sponsorflow.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ApiErrorResponse {

    private int status;

    private String message;

    private Map<String, String> errors;

    private LocalDateTime timestamp;

    public ApiErrorResponse(
            int status,
            String message,
            Map<String, String> errors
    ) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }
}