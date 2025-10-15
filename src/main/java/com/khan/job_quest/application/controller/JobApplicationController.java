package com.khan.job_quest.application.controller;

import com.khan.job_quest.application.dto.JobApplicationRequest;
import com.khan.job_quest.application.dto.JobApplicationResponse;
import com.khan.job_quest.application.service.JobApplicationService;
import com.khan.job_quest.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
            @Valid @ModelAttribute JobApplicationRequest request) throws IOException {

        JobApplicationResponse application = jobApplicationService.createJobApplication(jobId, request);

        ApiResponse<JobApplicationResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Job application submitted successfully",
                application,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<JobApplicationResponse>>> getAllJobApplications(
            @PageableDefault(size = 10, page = 0) Pageable pageable) {

        Page<JobApplicationResponse> applications = jobApplicationService.getAllJobApplications(pageable);

        ApiResponse<Page<JobApplicationResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Job applications retrieved successfully",
                applications,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }
}
