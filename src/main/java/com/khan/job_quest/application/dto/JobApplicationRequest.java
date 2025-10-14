package com.khan.job_quest.application.dto;

import org.springframework.web.multipart.MultipartFile;

public class JobApplicationRequest {
    private Long jobId;
    private String coverLetter;
    private MultipartFile resume;
}
