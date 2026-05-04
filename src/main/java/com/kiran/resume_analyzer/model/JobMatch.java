package com.kiran.resume_analyzer.model;

import java.util.List;

public record JobMatch(
        String title,
        String company,
        String applyLink,
        List<String> matchedSkills,
        List<String> missingSkills,
        int matchScore
){
}
