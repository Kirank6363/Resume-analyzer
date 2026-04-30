package com.kiran.resume_analyzer.dto;

public record ResumeUploadResponse(
        String message,
        String fileName,
        String contentType,
        long size
) {


}
