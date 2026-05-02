package com.kiran.resume_analyzer.service;

import com.kiran.resume_analyzer.model.CandidateProfile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResumeAnalysisService {

    private static final List<String> KNOWN_SKILLS = List.of(
            "java", "spring boot", "mysql", "sql", "javascript", "react",
            "html", "css", "git", "github", "docker", "rest api", "mongodb"
    );

    private static final List<String> KNOWN_ROLES = List.of(
            "backend developer", "java developer", "full stack developer",
            "software engineer", "web developer"
    );

    public CandidateProfile analyzeResume(String extractedText) {
        String normalizedText = extractedText.toLowerCase();

        List<String> matchedSkills = new ArrayList<>();
        List<String> matchedRoles = new ArrayList<>();
        List<String> experienceHighlights = new ArrayList<>();

        for (String skill : KNOWN_SKILLS) {
            if (normalizedText.contains(skill)) {
                matchedSkills.add(skill);
            }
        }

        for (String role : KNOWN_ROLES) {
            if (normalizedText.contains(role)) {
                matchedRoles.add(role);
            }
        }

        if (normalizedText.contains("intern")) {
            experienceHighlights.add("Internship experience");
        }

        if (normalizedText.contains("project")) {
            experienceHighlights.add("Project experience mentioned");
        }

        if (normalizedText.contains("year")) {
            experienceHighlights.add("Experience duration mentioned");
        }

        return new CandidateProfile(matchedSkills, matchedRoles, experienceHighlights);
    }
}
