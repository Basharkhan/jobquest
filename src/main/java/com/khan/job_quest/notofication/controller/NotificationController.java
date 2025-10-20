package com.khan.job_quest.notofication.controller;


import com.khan.job_quest.auth.service.AuthService;
import com.khan.job_quest.common.response.ApiResponse;
import com.khan.job_quest.notofication.dto.NotificationResponse;
import com.khan.job_quest.notofication.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final AuthService authService;

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYER', 'JOB_SEEKER')")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUserNotifications() {
        List<NotificationResponse> notifications = notificationService.getUserNotifications();

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Notifications fetched successfully",
                notifications,
                LocalDateTime.now()
        ));
    }

    @PutMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'JOB_SEEKER')")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);

        return ResponseEntity.ok(new ApiResponse<>(
                HttpStatus.OK.value(),
                "Notification marked as read",
                null,
                LocalDateTime.now()
        ));
    }
}
