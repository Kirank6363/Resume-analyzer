package com.kiran.resume_analyzer.dto;

import com.kiran.resume_analyzer.model.CandidateProfile;
import com.kiran.resume_analyzer.model.JobMatch;

import java.util.List;

public record ResumeUploadResponse(
        String message,
        String fileName,
        String contentType,
        long size,
        String extractedText,
        CandidateProfile candidateProfile,
        List<JobMatch> jobMatches
) {
}
