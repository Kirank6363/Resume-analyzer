package com.kiran.resume_analyzer.exception;

import com.kiran.resume_analyzer.dto.ErrorResponse;
import org.apache.tika.exception.TikaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(HttpClientErrorException.Unauthorized ex) {
        return buildResponse(
                "Adzuna authentication failed. Check your ADZUNA_APP_ID and ADZUNA_APP_KEY.",
                HttpStatus.BAD_GATEWAY
        );
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResponse> handleApiFailure(RestClientException ex) {
        return buildResponse(
                "Unable to fetch jobs from Adzuna right now. Please try again later.",
                HttpStatus.BAD_GATEWAY
        );
    }

    @ExceptionHandler({IOException.class, TikaException.class})
    public ResponseEntity<ErrorResponse> handleFileProcessing(Exception ex) {
        return buildResponse(
                "Unable to read the uploaded resume. Please upload a valid PDF or DOCX file.",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return buildResponse(
                "Something went wrong while processing the resume.",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<ErrorResponse> buildResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(message, status.value(), Instant.now()));
    }
}
