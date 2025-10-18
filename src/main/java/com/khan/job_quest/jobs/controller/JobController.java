package com.khan.job_quest.jobs.controller;

import com.khan.job_quest.common.response.ApiResponse;
import com.khan.job_quest.jobs.dto.JobRequest;
import com.khan.job_quest.jobs.dto.JobResponse;
import com.khan.job_quest.jobs.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<JobResponse>> createJob(@Valid @RequestBody JobRequest request) {
        JobResponse job = jobService.createJob(request);

        ApiResponse<JobResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Job created successfully",
                job,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<JobResponse>> updateJob(@PathVariable Long id, @Valid @RequestBody JobRequest request) {
        JobResponse job = jobService.updateJob(id, request);

        ApiResponse<JobResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Job updated successfully",
                job,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<Page<JobResponse>>> getAllJobs(Pageable pageable) {
        Page<JobResponse> jobs = jobService.getAllJobs(pageable);

        ApiResponse<Page<JobResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Jobs retrieved successfully",
                jobs,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYER') or hasRole('JOB_SEEKER')")
    public ResponseEntity<ApiResponse<JobResponse>> getJobById(@PathVariable Long id) {
        JobResponse jobs = jobService.getJobById(id);

        ApiResponse<JobResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Job retrieved successfully",
                jobs,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);

        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Job deleted successfully",
                null,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPLOYER') or hasRole('JOB_SEEKER')")
    public ResponseEntity<ApiResponse<Page<JobResponse>>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer minSalary,
            @RequestParam(required = false) Integer maxSalary,
            Pageable pageable
    ) {
        Page<JobResponse> jobs = jobService.searchJobs(keyword, location, minSalary, maxSalary, pageable);

        ApiResponse<Page<JobResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Jobs retrieved successfully",
                jobs,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }
}
