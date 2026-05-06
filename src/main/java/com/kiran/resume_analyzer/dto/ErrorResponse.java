package com.kiran.resume_analyzer.dto;

import java.time.Instant;

public record ErrorResponse(
        String message,
        int status,
        Instant timestamp
) {
}
