package com.khan.job_quest.jobs.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private Double salary;
    private String employerName;
    private String companyName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
