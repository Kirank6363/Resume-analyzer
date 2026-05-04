package com.kiran.resume_analyzer.model;

import java.util.List;

public record JobPosting (
        String title,
        String company,
        String applyLink,
        String description,
        List<String> requiredSkills,
        String role
){
}
