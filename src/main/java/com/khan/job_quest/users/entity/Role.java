package com.khan.job_quest.users.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    EMPLOYER,
    JOB_SEEKER;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
