package com.khan.job_quest.application.controller;

import com.khan.job_quest.application.dto.JobApplicationResponse;
import com.khan.job_quest.application.service.JobApplicationService;
import com.khan.job_quest.common.response.ApiResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    @PostMapping("/apply/{jobId}")
    @PreAuthorize("hasRole('JOB_SEEKER')")
    public ResponseEntity<ApiResponse<JobApplicationResponse>> applyForJob(
            @PathVariable Long jobId,
            @RequestParam("resume") MultipartFile resume,
            @RequestParam("coverLetter") @NotBlank String coverLetter
    ) throws IOException {

        JobApplicationResponse application = jobApplicationService.createJobApplication(jobId, resume, coverLetter);

        ApiResponse<JobApplicationResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Job application submitted successfully",
                application,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }
}
