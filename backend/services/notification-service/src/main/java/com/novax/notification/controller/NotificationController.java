package com.novax.notification.controller;

import com.novax.common.core.result.Result;
import com.novax.notification.dto.SendNotificationDTO;
import com.novax.notification.entity.NotificationMessage;
import com.novax.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "通知管理")
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "发送通知")
    @PostMapping("/send")
    public Result<Boolean> sendNotification(@Valid @RequestBody SendNotificationDTO dto) {
        return Result.success(notificationService.sendNotification(dto));
    }

    @Operation(summary = "获取未读消息")
    @GetMapping("/unread/{userId}")
    public Result<List<NotificationMessage>> getUnreadMessages(@PathVariable Long userId) {
        return Result.success(notificationService.getUnreadMessages(userId));
    }

    @Operation(summary = "标记为已读")
    @PostMapping("/{messageId}/read")
    public Result<Boolean> markAsRead(@PathVariable Long messageId) {
        return Result.success(notificationService.markAsRead(messageId));
    }
}
