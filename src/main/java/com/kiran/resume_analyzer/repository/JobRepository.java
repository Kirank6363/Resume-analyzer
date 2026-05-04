package com.kiran.resume_analyzer.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kiran.resume_analyzer.model.CandidateProfile;
import com.kiran.resume_analyzer.model.JobPosting;
import com.kiran.resume_analyzer.service.ResumeAnalysisService;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JobRepository {

    private final RestClient restClient;
    private final ResumeAnalysisService resumeAnalysisService;
    private final String baseUrl;
    private final String country;
    private final int resultsPerPage;
    private final String appId;
    private final String appKey;

    public JobRepository(ResumeAnalysisService resumeAnalysisService,
                         @Value("${adzuna.base-url}") String baseUrl,
                         @Value("${adzuna.country}") String country,
                         @Value("${adzuna.results-per-page}") int resultsPerPage,
                         @Value("${adzuna.app-id}") String appId,
                         @Value("${adzuna.app-key}") String appKey) {
        this.resumeAnalysisService = resumeAnalysisService;
        this.baseUrl = baseUrl;
        this.country = country;
        this.resultsPerPage = resultsPerPage;
        this.appId = appId;
        this.appKey = appKey;
        this.restClient = RestClient.builder().build();
    }

    public List<JobPosting> findAllJobs(CandidateProfile candidateProfile) {
        validateCredentials();

        String searchQuery = buildSearchQuery(candidateProfile);

        URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                .path("/jobs/{country}/search/1")
                .queryParam("app_id", appId)
                .queryParam("app_key", appKey)
                .queryParam("results_per_page", resultsPerPage)
                .queryParam("what", searchQuery)
                .queryParam("content-type", "application/json")
                .build(country);

        AdzunaSearchResponse response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(AdzunaSearchResponse.class);

        if (response == null || response.results() == null) {
            return Collections.emptyList();
        }

        return response.results().stream()
                .map(this::toJobPosting)
                .collect(Collectors.toList());
    }

    private void validateCredentials() {
        if (appId == null || appId.isBlank() || appKey == null || appKey.isBlank()) {
            throw new IllegalStateException(
                    "Adzuna credentials are missing. Set ADZUNA_APP_ID and ADZUNA_APP_KEY before running the app."
            );
        }
    }

    private String buildSearchQuery(CandidateProfile candidateProfile) {
        if (!candidateProfile.roles().isEmpty()) {
            return candidateProfile.roles().getFirst();
        }

        if (!candidateProfile.skills().isEmpty()) {
            return String.join(" ", candidateProfile.skills().stream().limit(3).toList());
        }

        return "software engineer";
    }

    private JobPosting toJobPosting(AdzunaJobResult result) {
        String description = result.description() == null ? "" : result.description();
        String title = result.title() == null ? "" : result.title();
        List<String> extractedSkills = resumeAnalysisService.extractSkills(title + " " + description);
        List<String> extractedRoles = resumeAnalysisService.extractRoles(title + " " + description);

        String resolvedCompany = result.company() != null && result.company().displayName() != null
                ? result.company().displayName()
                : "Unknown Company";

        String resolvedRole = !extractedRoles.isEmpty() ? extractedRoles.getFirst() : title.toLowerCase();

        return new JobPosting(
                title,
                resolvedCompany,
                result.redirectUrl(),
                description,
                extractedSkills,
                resolvedRole
        );
    }

    private record AdzunaSearchResponse(List<AdzunaJobResult> results) {
    }

    private record AdzunaJobResult(
            String title,
            String description,
            @JsonProperty("redirect_url")
            String redirectUrl,
            AdzunaCompany company
    ) {
    }

    private record AdzunaCompany(@JsonProperty("display_name") String displayName) {
    }
}
