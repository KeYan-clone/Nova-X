package com.novax.notification.service;

import com.novax.notification.dto.SendNotificationDTO;
import com.novax.notification.entity.NotificationMessage;

import java.util.List;

public interface NotificationService {

    Boolean sendNotification(SendNotificationDTO dto);

    Boolean sendSms(Long userId, String phone, String content);

    Boolean sendEmail(Long userId, String email, String subject, String content);

    Boolean sendPush(Long userId, String title, String content);

    Boolean sendInApp(Long userId, String title, String content);

    List<NotificationMessage> getUnreadMessages(Long userId);

    Boolean markAsRead(Long messageId);
}
