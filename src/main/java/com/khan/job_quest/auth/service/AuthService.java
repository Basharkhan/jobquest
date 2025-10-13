package com.khan.job_quest.auth.service;

import com.khan.job_quest.auth.dto.AuthResponse;
import com.khan.job_quest.auth.dto.LoginRequest;
import com.khan.job_quest.auth.dto.RegisterRequest;
import com.khan.job_quest.auth.dto.UserDetailsDto;
import com.khan.job_quest.auth.jwt.JwtService;
import com.khan.job_quest.common.exception.InvalidCredentialsException;
import com.khan.job_quest.common.exception.ResourceAlreadyExistsException;
import com.khan.job_quest.common.exception.ResourceNotFoundException;
import com.khan.job_quest.users.entity.Role;
import com.khan.job_quest.users.entity.User;
import com.khan.job_quest.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse registerAdmin(RegisterRequest request) {
        return register(request, Role.ADMIN);
    }

    @Transactional
    public AuthResponse registerEmployee(RegisterRequest request) {
        return register(request, Role.EMPLOYER);
    }

    @Transactional
    public AuthResponse registerJobSeeker(RegisterRequest request) {
        return register(request, Role.JOB_SEEKER);
    }

    public AuthResponse register(RegisterRequest request, Role role) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("User already exists with email: " + request.getEmail());
        }

        // create user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        // save user
        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                .email(savedUser.getEmail())
                .name(savedUser.getName())
                .role(savedUser.getRole())
                .build();

        return AuthResponse.builder()
                .token(token)
                .userDetailsDto(userDetailsDto)
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + loginRequest.getEmail()));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);

        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();

        return AuthResponse.builder()
                .token(token)
                .userDetailsDto(userDetailsDto)
                .build();
    }
}
