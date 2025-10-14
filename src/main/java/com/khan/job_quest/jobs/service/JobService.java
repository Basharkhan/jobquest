package com.khan.job_quest.jobs.service;

import com.khan.job_quest.auth.service.AuthService;
import com.khan.job_quest.common.exception.PermissionDeniedException;
import com.khan.job_quest.common.exception.ResourceNotFoundException;
import com.khan.job_quest.jobs.dto.JobRequest;
import com.khan.job_quest.jobs.dto.JobResponse;
import com.khan.job_quest.jobs.entity.Job;
import com.khan.job_quest.jobs.repository.JobRepository;
import com.khan.job_quest.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final AuthService authService;

    public JobResponse createJob(JobRequest request) {
        User employer = authService.getAuthenticatedUser();

        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .salary(request.getSalary())
                .employer(employer)
                .build();

        Job savedJob = jobRepository.save(job);

        return mapToJobResponse(savedJob);
    }

    public JobResponse updateJob(Long id, JobRequest request) {
        User currentUser = authService.getAuthenticatedUser();

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));

        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");
        boolean isEmployer = currentUser.getRole().name().equals("EMPLOYER") && job.getEmployer().getId().equals(currentUser.getId());

        if (!isAdmin && !isEmployer) {
            throw new PermissionDeniedException("You do not have permission to update this job.");
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());

        Job updatedJob = jobRepository.save(job);

        return mapToJobResponse(updatedJob);
    }


    public Page<JobResponse> getAllJobs(Pageable pageable) {
        return jobRepository.findAll(pageable).map(this::mapToJobResponse);
    }

    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        return mapToJobResponse(job);
    }

    public void deleteJob(Long id) {
        User currentUser = authService.getAuthenticatedUser();

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));

        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");
        boolean isEmployer = currentUser.getRole().name().equals("EMPLOYER") && job.getEmployer().getId().equals(currentUser.getId());

        if (!isAdmin && !isEmployer) {
            throw new PermissionDeniedException("You do not have permission to delete this job.");
        }

        jobRepository.delete(job);
    }

    private JobResponse mapToJobResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .salary(job.getSalary())
                .employerName(job.getEmployer() != null ? job.getEmployer().getName() : null)
                .companyName(job.getCompanyName())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
}
