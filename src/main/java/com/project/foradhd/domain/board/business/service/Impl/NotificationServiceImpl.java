package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.NotificationService;
import com.project.foradhd.domain.board.persistence.entity.Notification;
import com.project.foradhd.domain.board.persistence.repository.NotificationRepository;
import com.project.foradhd.global.util.SseEmitters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final SseEmitters sseEmitters;

    @Override
    public void createNotification(String userId, String message) {
        Notification notification = Notification.builder()
                .userId(userId)
                .message(message)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
        sseEmitters.sendNotification(userId, message); // SSE 알림 전송
    }

    @Override
    public List<Notification> getNotifications(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
