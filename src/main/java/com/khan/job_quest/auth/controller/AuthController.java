package com.khan.job_quest.auth.controller;

import com.khan.job_quest.auth.dto.AuthResponse;
import com.khan.job_quest.auth.dto.LoginRequest;
import com.khan.job_quest.auth.dto.RegisterRequest;
import com.khan.job_quest.auth.service.AuthService;
import com.khan.job_quest.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final Environment env;

    @PostMapping("/register/admin")
    public ResponseEntity<ApiResponse<AuthResponse>> registerAdmin(@RequestHeader("X-SETUP-KEY") String setupKey,
                                                                   @Valid @RequestBody RegisterRequest request) {
        String expectedKey = env.getProperty("app.setup.key");

        if (expectedKey == null || !expectedKey.equals(setupKey)) {
            throw new AccessDeniedException("Access denied");
        }

        AuthResponse authResponse = authService.registerAdmin(request);

        ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Admin registered successfully",
                authResponse,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/register/employee")
    public ResponseEntity<ApiResponse<AuthResponse>> registerEmployee(@Valid @RequestBody RegisterRequest request) {
        AuthResponse authResponse = authService.registerEmployee(request);

        ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Employee registered successfully",
                authResponse,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/register/job-seeker")
    public ResponseEntity<ApiResponse<AuthResponse>> registerJobSeeker(@Valid @RequestBody RegisterRequest request) {
        AuthResponse authResponse = authService.registerJobSeeker(request);

        ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Job Seeker registered successfully",
                authResponse,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);

        ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Login successful",
                authResponse,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(apiResponse);
    }
}
