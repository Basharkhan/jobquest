package com.khan.job_quest.application.dto;

import com.khan.job_quest.application.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateApplicationStatusRequest {
    @NotNull(message = "Status is required")
    private ApplicationStatus status;
}
