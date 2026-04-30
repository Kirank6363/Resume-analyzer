package com.kiran.resume_analyzer.controller;


import com.kiran.resume_analyzer.dto.ResumeUploadResponse;
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

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {


    private final ResumeParsingService resumeParsingService;

    @PostMapping("/upload")
    public ResponseEntity<ResumeUploadResponse> uploadResume(@RequestParam("file") MultipartFile file) throws IOException, TikaException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ResumeUploadResponse(
                            "File is empty. Please upload a valid resume.",
                            null,
                            null,
                            0,
                            null
                    )
            );
        }
        String extractedText = resumeParsingService.extractText(file);

        ResumeUploadResponse response = new ResumeUploadResponse(
                "Resume uploaded successfully.",
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                extractedText
        );

        return ResponseEntity.ok(response);
    }
}
