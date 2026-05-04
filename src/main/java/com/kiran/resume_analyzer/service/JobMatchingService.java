package com.kiran.resume_analyzer.service;

import com.kiran.resume_analyzer.model.CandidateProfile;
import com.kiran.resume_analyzer.model.JobMatch;
import com.kiran.resume_analyzer.model.JobPosting;
import com.kiran.resume_analyzer.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class JobMatchingService {

    private final JobRepository jobRepository;
    private final ResumeAnalysisService resumeAnalysisService;

    public JobMatchingService(JobRepository jobRepository, ResumeAnalysisService resumeAnalysisService) {
        this.jobRepository = jobRepository;
        this.resumeAnalysisService = resumeAnalysisService;
    }

    public List<JobMatch> findMatches(CandidateProfile candidateProfile) {
        List<JobPosting> jobs = jobRepository.findAllJobs(candidateProfile);
        List<JobMatch> matches = new ArrayList<>();

        for (JobPosting job : jobs) {
            List<String> matchedSkills = new ArrayList<>();
            List<String> missingSkills = new ArrayList<>();
            List<String> requiredSkills = job.requiredSkills().isEmpty()
                    ? resumeAnalysisService.extractSkills(job.title() + " " + job.description())
                    : job.requiredSkills();

            for (String skill : candidateProfile.skills()) {
                if (requiredSkills.contains(skill)) {
                    matchedSkills.add(skill);
                }
            }

            for (String requiredSkill : requiredSkills) {
                if (!candidateProfile.skills().contains(requiredSkill)) {
                    missingSkills.add(requiredSkill);
                }
            }

            boolean roleMatched = candidateProfile.roles().stream()
                    .anyMatch(role -> role.equalsIgnoreCase(job.role()));

            int score = calculateMatchScore(requiredSkills, matchedSkills, roleMatched);

            if (score > 0) {
                matches.add(new JobMatch(
                        job.title(),
                        job.company(),
                        job.applyLink(),
                        matchedSkills,
                        missingSkills,
                        score
                ));
            }
        }

        matches.sort(Comparator.comparingInt(JobMatch::matchScore).reversed());
        return matches;
    }

    private int calculateMatchScore(List<String> requiredSkills, List<String> matchedSkills, boolean roleMatched) {
        if (requiredSkills.isEmpty()) {
            return roleMatched ? 70 : 0;
        }

        double skillScore = ((double) matchedSkills.size() / requiredSkills.size()) * 100;

        if (roleMatched) {
            skillScore += 15;
        }

        return (int) Math.min(Math.round(skillScore), 100);
    }
}
