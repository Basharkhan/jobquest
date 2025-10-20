package com.khan.job_quest.notofication.service;

import com.khan.job_quest.auth.service.AuthService;
import com.khan.job_quest.common.exception.PermissionDeniedException;
import com.khan.job_quest.common.exception.ResourceNotFoundException;
import com.khan.job_quest.notofication.dto.NotificationResponse;
import com.khan.job_quest.notofication.entity.Notification;
import com.khan.job_quest.notofication.repository.NotificationRepository;
import com.khan.job_quest.users.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final AuthService authService;

    public Notification createNotification(User recipient, String title, String message) {
        Notification notification = Notification.builder()
                .recipient(recipient)
                .title(title)
                .message(message)
                .build();

        return notificationRepository.save(notification);
    }

    public List<NotificationResponse> getUserNotifications() {
        User user = authService.getAuthenticatedUser();
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void markAsRead(Long id) {
        User user = authService.getAuthenticatedUser();

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        if (!notification.getRecipient().getId().equals(user.getId())) {
            throw new PermissionDeniedException("You cannot mark this notification");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
