package com.novax.notification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.novax.common.core.exception.BusinessException;
import com.novax.notification.dto.SendNotificationDTO;
import com.novax.notification.entity.NotificationMessage;
import com.novax.notification.entity.NotificationTemplate;
import com.novax.notification.mapper.NotificationMessageMapper;
import com.novax.notification.mapper.NotificationTemplateMapper;
import com.novax.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMessageMapper messageMapper;
    private final NotificationTemplateMapper templateMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean sendNotification(SendNotificationDTO dto) {
        log.info("发送通知, 用户ID: {}, 类型: {}, 模板: {}",
                dto.getUserId(), dto.getMessageType(), dto.getTemplateCode());

        LambdaQueryWrapper<NotificationTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NotificationTemplate::getTemplateCode, dto.getTemplateCode())
                .eq(NotificationTemplate::getEnabled, true);
        NotificationTemplate template = templateMapper.selectOne(wrapper);

        if (template == null) {
            throw new BusinessException("通知模板不存在或未启用");
        }

        String content = renderTemplate(template.getContent(), dto.getParams());
        String title = renderTemplate(template.getTitle(), dto.getParams());

        NotificationMessage message = new NotificationMessage();
        message.setUserId(dto.getUserId());
        message.setMessageType(dto.getMessageType());
        message.setTemplateId(template.getId());
        message.setTitle(title);
        message.setContent(content);
        message.setReceiver(dto.getReceiver());
        message.setBusinessType(dto.getBusinessType());
        message.setBusinessId(dto.getBusinessId());
        message.setSendStatus("PENDING");
        message.setRetryCount(0);
        message.setIsRead(false);

        messageMapper.insert(message);

        switch (dto.getMessageType()) {
            case "SMS" -> sendSmsInternal(message);
            case "EMAIL" -> sendEmailInternal(message);
            case "PUSH" -> sendPushInternal(message);
            case "IN_APP" -> message.setSendStatus("SUCCESS");
            default -> throw new BusinessException("不支持的消息类型");
        }

        message.setSendTime(LocalDateTime.now());
        messageMapper.updateById(message);

        return true;
    }

    @Override
    public Boolean sendSms(Long userId, String phone, String content) {
        NotificationMessage message = new NotificationMessage();
        message.setUserId(userId);
        message.setMessageType("SMS");
        message.setContent(content);
        message.setReceiver(phone);
        message.setSendStatus("PENDING");
        message.setRetryCount(0);
        message.setIsRead(true);

        messageMapper.insert(message);
        sendSmsInternal(message);
        message.setSendTime(LocalDateTime.now());
        messageMapper.updateById(message);

        return true;
    }

    @Override
    public Boolean sendEmail(Long userId, String email, String subject, String content) {
        NotificationMessage message = new NotificationMessage();
        message.setUserId(userId);
        message.setMessageType("EMAIL");
        message.setTitle(subject);
        message.setContent(content);
        message.setReceiver(email);
        message.setSendStatus("PENDING");
        message.setRetryCount(0);
        message.setIsRead(true);

        messageMapper.insert(message);
        sendEmailInternal(message);
        message.setSendTime(LocalDateTime.now());
        messageMapper.updateById(message);

        return true;
    }

    @Override
    public Boolean sendPush(Long userId, String title, String content) {
        NotificationMessage message = new NotificationMessage();
        message.setUserId(userId);
        message.setMessageType("PUSH");
        message.setTitle(title);
        message.setContent(content);
        message.setSendStatus("PENDING");
        message.setRetryCount(0);
        message.setIsRead(false);

        messageMapper.insert(message);
        sendPushInternal(message);
        message.setSendTime(LocalDateTime.now());
        messageMapper.updateById(message);

        return true;
    }

    @Override
    public Boolean sendInApp(Long userId, String title, String content) {
        NotificationMessage message = new NotificationMessage();
        message.setUserId(userId);
        message.setMessageType("IN_APP");
        message.setTitle(title);
        message.setContent(content);
        message.setSendStatus("SUCCESS");
        message.setSendTime(LocalDateTime.now());
        message.setRetryCount(0);
        message.setIsRead(false);

        messageMapper.insert(message);
        return true;
    }

    @Override
    public List<NotificationMessage> getUnreadMessages(Long userId) {
        LambdaQueryWrapper<NotificationMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NotificationMessage::getUserId, userId)
                .eq(NotificationMessage::getIsRead, false)
                .orderByDesc(NotificationMessage::getCreateTime);
        return messageMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean markAsRead(Long messageId) {
        NotificationMessage message = messageMapper.selectById(messageId);
        if (message != null && !message.getIsRead()) {
            message.setIsRead(true);
            message.setReadTime(LocalDateTime.now());
            messageMapper.updateById(message);
        }
        return true;
    }

    private void sendSmsInternal(NotificationMessage message) {
        try {
            log.info("发送短信: {}", message.getReceiver());
            message.setSendStatus("SUCCESS");
        } catch (Exception e) {
            log.error("短信发送失败", e);
            message.setSendStatus("FAILED");
            message.setFailureReason(e.getMessage());
        }
    }

    private void sendEmailInternal(NotificationMessage message) {
        try {
            log.info("发送邮件: {}", message.getReceiver());
            message.setSendStatus("SUCCESS");
        } catch (Exception e) {
            log.error("邮件发送失败", e);
            message.setSendStatus("FAILED");
            message.setFailureReason(e.getMessage());
        }
    }

    private void sendPushInternal(NotificationMessage message) {
        try {
            log.info("发送推送: 用户{}", message.getUserId());
            message.setSendStatus("SUCCESS");
        } catch (Exception e) {
            log.error("推送发送失败", e);
            message.setSendStatus("FAILED");
            message.setFailureReason(e.getMessage());
        }
    }

    private String renderTemplate(String template, Map<String, String> params) {
        if (template == null || params == null) {
            return template;
        }

        String result = template;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result = result.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }
}
