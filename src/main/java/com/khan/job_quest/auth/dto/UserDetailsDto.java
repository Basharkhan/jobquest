package com.khan.job_quest.auth.dto;

import com.khan.job_quest.users.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailsDto {
    private String email;
    private String name;
    private Role role;
}
