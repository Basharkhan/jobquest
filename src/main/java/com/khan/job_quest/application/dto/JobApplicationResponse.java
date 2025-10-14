package com.khan.job_quest.application.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class JobApplicationResponse {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private Long applicantId;
    private String applicantName;
    private String applicantEmail;
    private String coverLetter;
    private String resumeLink;
    private LocalDateTime appliedAt;
    private String status;
}
