package com.khan.job_quest.application.service;

import com.khan.job_quest.application.dto.JobApplicationResponse;
import com.khan.job_quest.application.entity.ApplicationStatus;
import com.khan.job_quest.application.entity.JobApplication;
import com.khan.job_quest.application.repository.JobApplicationRepository;
import com.khan.job_quest.auth.service.AuthService;
import com.khan.job_quest.common.exception.ResourceAlreadyExistsException;
import com.khan.job_quest.common.exception.ResourceNotFoundException;
import com.khan.job_quest.jobs.entity.Job;
import com.khan.job_quest.jobs.repository.JobRepository;
import com.khan.job_quest.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobApplicationService {
    private final JobApplicationRepository jobApplicationRepository;
    private final JobRepository jobRepository;
    private final AuthService authService;

    public JobApplicationResponse createJobApplication(Long jobId, MultipartFile resume, String coverLetter) throws IOException {
        User applicant = authService.getAuthenticatedUser();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        Optional<JobApplication> existingApplication = jobApplicationRepository
                .findByJobIdAndApplicantId(jobId, applicant.getId());

        // check duplicate application
        if (existingApplication.isPresent()) {
            throw new ResourceAlreadyExistsException("Job application already exists with id: " + jobId);
        }

        // save resume to uploads/resumes
        String uploadDir = "uploads/resumes/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = UUID.randomUUID().toString() + "." + resume.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        resume.transferTo(filePath.toFile());

        // save job application
        JobApplication jobApplication = JobApplication.builder()
                .job(job)
                .applicant(applicant)
                .resumeLink(filePath.toString())
                .coverLetter(coverLetter)
                .status(ApplicationStatus.PENDING)
                .build();

        JobApplication savedApplication = jobApplicationRepository.save(jobApplication);

        return mapToJobApplicationResponse(savedApplication);
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
