package com.kiran.resume_analyzer.controller;


import com.kiran.resume_analyzer.dto.ResumeUploadResponse;
import com.kiran.resume_analyzer.model.CandidateProfile;
import com.kiran.resume_analyzer.model.JobMatch;
import com.kiran.resume_analyzer.service.JobMatchingService;
import com.kiran.resume_analyzer.service.ResumeAnalysisService;
import com.kiran.resume_analyzer.service.ResumeParsingService;
import lombok.RequiredArgsConstructor;
import org.apache.tika.exception.TikaException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {


    private final ResumeParsingService resumeParsingService;
    private final ResumeAnalysisService resumeAnalysisService;
    private final JobMatchingService jobMatchingService;

    @PostMapping("/upload")
    public ResponseEntity<ResumeUploadResponse> uploadResume(@RequestParam("file") MultipartFile file) throws IOException, TikaException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ResumeUploadResponse(
                            "File is empty. Please upload a valid resume.",
                            null,
                            null,
                            0,
                            null,
                            null,
                            null
                    )
            );
        }
        String extractedText = resumeParsingService.extractText(file);
        CandidateProfile candidateProfile = resumeAnalysisService.analyzeResume(extractedText);
        List<JobMatch> jobMatches = jobMatchingService.findMatches(candidateProfile);

        ResumeUploadResponse response = new ResumeUploadResponse(
                "Resume uploaded, parsed, and matched successfully.",
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                extractedText,
                candidateProfile,
                jobMatches
        );

        return ResponseEntity.ok(response);
    }
}
