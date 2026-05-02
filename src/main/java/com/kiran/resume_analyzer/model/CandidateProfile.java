package com.kiran.resume_analyzer.model;

import java.util.List;

public record CandidateProfile(
        List<String> skills,
        List<String> roles,
        List<String> experienceHighlights
) {
}
