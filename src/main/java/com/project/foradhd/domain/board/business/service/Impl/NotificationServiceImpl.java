package com.project.foradhd.domain.board.business.service.Impl;

import com.project.foradhd.domain.board.business.service.NotificationService;
import com.project.foradhd.domain.board.persistence.entity.Notification;
import com.project.foradhd.domain.board.persistence.repository.NotificationRepository;
import com.project.foradhd.global.exception.BusinessException;
import com.project.foradhd.global.exception.ErrorCode;
import com.project.foradhd.global.util.SseEmitters;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        sseEmitters.sendNotification(userId, message);
    }

    @Override
    public List<Notification> getNotifications(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_NOTIFICATION));

        Notification updatedNotification = notification.toBuilder()
                .isRead(true)
                .build();

        notificationRepository.save(updatedNotification);
    }
}
