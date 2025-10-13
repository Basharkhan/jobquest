package com.khan.job_quest.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private UserDetailsDto userDetailsDto;
}
