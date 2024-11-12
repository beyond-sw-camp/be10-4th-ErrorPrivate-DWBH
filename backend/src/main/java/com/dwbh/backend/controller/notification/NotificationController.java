package com.dwbh.backend.controller.notification;

import com.dwbh.backend.common.util.AuthUtil;
import com.dwbh.backend.dto.chat.ChatDTO;
import com.dwbh.backend.dto.notification.NotificationResponse;
import com.dwbh.backend.service.UserService;
import com.dwbh.backend.service.notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
@Tag(name = "Notification API", description = "알림 API")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping("/{notificationSeq}")
    @Operation(summary = "알림 상세 조회 (채팅방 입장)")
    public ChatDTO.Response readNotification(@PathVariable Long notificationSeq) {

        return notificationService.readNotification(notificationSeq);
    }

    @GetMapping
    @Operation(summary = "알림 사용자에게 보내기 (최근 2주)")
    public ResponseEntity<NotificationResponse> readNotificationList() {
        Long userSeq = userService.getUserSeq(AuthUtil.getAuthUser());

        NotificationResponse notificationDTOList = notificationService.readNotificationList(userSeq);

        return ResponseEntity.ok(notificationDTOList);
    }

}
