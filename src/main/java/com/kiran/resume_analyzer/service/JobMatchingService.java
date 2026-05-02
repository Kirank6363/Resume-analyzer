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

    public JobMatchingService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<JobMatch> findMatches(CandidateProfile candidateProfile) {
        List<JobPosting> jobs = jobRepository.findAllJobs();
        List<JobMatch> matches = new ArrayList<>();

        for (JobPosting job : jobs) {
            List<String> matchedSkills = new ArrayList<>();

            for (String skill : candidateProfile.skills()) {
                if (job.requiredSkills().contains(skill)) {
                    matchedSkills.add(skill);
                }
            }

            int score = matchedSkills.size();

            boolean roleMatched = candidateProfile.roles().stream()
                    .anyMatch(role -> role.equalsIgnoreCase(job.role()));

            if (roleMatched) {
                score += 2;
            }

            if (!matchedSkills.isEmpty() || roleMatched) {
                matches.add(new JobMatch(
                        job.title(),
                        job.company(),
                        job.applyLink(),
                        matchedSkills,
                        score
                ));
            }
        }

        matches.sort(Comparator.comparingInt(JobMatch::matchScore).reversed());
        return matches;
    }
}
