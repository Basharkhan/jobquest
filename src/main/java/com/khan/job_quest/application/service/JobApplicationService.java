package com.khan.job_quest.application.service;

import com.khan.job_quest.application.dto.JobApplicationRequest;
import com.khan.job_quest.application.dto.JobApplicationResponse;
import com.khan.job_quest.application.entity.ApplicationStatus;
import com.khan.job_quest.application.entity.JobApplication;
import com.khan.job_quest.application.repository.JobApplicationRepository;
import com.khan.job_quest.auth.service.AuthService;
import com.khan.job_quest.common.exception.PermissionDeniedException;
import com.khan.job_quest.common.exception.ResourceAlreadyExistsException;
import com.khan.job_quest.common.exception.ResourceNotFoundException;
import com.khan.job_quest.jobs.entity.Job;
import com.khan.job_quest.jobs.repository.JobRepository;
import com.khan.job_quest.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobApplicationService {
    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final AuthService authService;

    public JobApplicationResponse createJobApplication(Long jobId, JobApplicationRequest request) throws IOException {
        User applicant = authService.getAuthenticatedUser();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        if (jobApplicationRepository.existsByJobIdAndApplicantId(jobId, applicant.getId())) {
            throw new ResourceAlreadyExistsException("You already applied for this job.");
        }

        // save resume to uploads/resumes
        String uploadDir = "uploads/resumes/";
        Files.createDirectories(Paths.get(uploadDir));

        String originalFilename = request.getResume().getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
        String fileName = UUID.randomUUID() + "." + extension;
        Path filePath = Paths.get(uploadDir, fileName);
        request.getResume().transferTo(filePath);

        JobApplication jobApplication = JobApplication.builder()
                .job(job)
                .applicant(applicant)
                .resumeLink(filePath.toString())
                .coverLetter(request.getCoverLetter())
                .status(ApplicationStatus.PENDING)
                .build();

        JobApplication savedApplication = jobApplicationRepository.save(jobApplication);

        return mapToJobApplicationResponse(savedApplication);
    }

    public Page<JobApplicationResponse> getAllJobApplications(Pageable pageable) {
        return  jobApplicationRepository.findAll(pageable).map(this::mapToJobApplicationResponse);
    }

    public Page<JobApplicationResponse> getAllJobApplicationsByEmployer(Pageable pageable) {
        User employer = authService.getAuthenticatedUser();
        return jobApplicationRepository.findByJob_EmployerId(employer.getId(), pageable)
                .map(this::mapToJobApplicationResponse);
    }

    public Page<JobApplicationResponse> getAllJobApplicationsByApplicant(Pageable pageable) {
        User applicant = authService.getAuthenticatedUser();
        return jobApplicationRepository.findByApplicantId(applicant.getId(), pageable)
                .map(this::mapToJobApplicationResponse);
    }

    public JobApplicationResponse updateJobApplicationStatus(Long id, ApplicationStatus status) {
        JobApplication jobApplication = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found with id: " + id));

        jobApplication.setStatus(status);

        JobApplication updatedApplication = jobApplicationRepository.save(jobApplication);

        return  mapToJobApplicationResponse(updatedApplication);
    }

    public void deleteApplication(Long id) {
        User user = authService.getAuthenticatedUser();

        JobApplication application = jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getApplicant().getId().equals(user.getId())) {
            throw new PermissionDeniedException("You can only delete your own application");
        }

        jobApplicationRepository.delete(application);
    }

    private JobApplicationResponse mapToJobApplicationResponse(JobApplication jobApplication) {
        return JobApplicationResponse.builder()
                .id(jobApplication.getId())
                .jobId(jobApplication.getJob().getId())
                .jobTitle(jobApplication.getJob().getTitle())
                .applicantId(jobApplication.getApplicant().getId())
                .applicantName(jobApplication.getApplicant().getName())
                .applicantEmail(jobApplication.getApplicant().getEmail())
                .coverLetter(jobApplication.getCoverLetter())
                .resumeLink(jobApplication.getResumeLink())
                .appliedAt(jobApplication.getCreatedAt())
                .status(jobApplication.getStatus().name())
                .build();
    }
}
