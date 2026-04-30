package com.kiran.resume_analyzer.controller;


import com.kiran.resume_analyzer.dto.ResumeUploadResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    @PostMapping("/upload")
    public ResponseEntity<ResumeUploadResponse> uploadResume(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ResumeUploadResponse(
                            "File is empty. Please upload a valid resume.",
                            null,
                            null,
                            0
                    )
            );
        }

        ResumeUploadResponse response = new ResumeUploadResponse(
                "Resume uploaded successfully.",
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize()
        );

        return ResponseEntity.ok(response);
    }
}
