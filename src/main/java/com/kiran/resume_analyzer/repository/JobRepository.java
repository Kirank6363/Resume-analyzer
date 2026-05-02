package com.kiran.resume_analyzer.repository;

import com.kiran.resume_analyzer.model.JobPosting;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobRepository {

    public List<JobPosting> findAllJobs() {
        return List.of(
                new JobPosting(
                        "Java Backend Developer",
                        "TechNova",
                        "https://example.com/jobs/java-backend-developer",
                        List.of("java", "spring boot", "mysql", "rest api"),
                        "backend developer"
                ),
                new JobPosting(
                        "Full Stack Developer",
                        "CodeCraft",
                        "https://example.com/jobs/full-stack-developer",
                        List.of("java", "javascript", "react", "sql"),
                        "full stack developer"
                ),
                new JobPosting(
                        "Software Engineer Intern",
                        "InnovateX",
                        "https://example.com/jobs/software-engineer-intern",
                        List.of("java", "git", "html", "css"),
                        "software engineer"
                )
        );
    }
}
