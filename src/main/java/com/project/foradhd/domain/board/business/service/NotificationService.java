package com.project.foradhd.domain.board.business.service;

import com.project.foradhd.domain.board.persistence.entity.Notification;
import java.util.List;

public interface NotificationService {
    void createNotification(String userId, String message);
    List<Notification> getNotifications(String userId);
    void markAsRead(Long notificationId);
}
