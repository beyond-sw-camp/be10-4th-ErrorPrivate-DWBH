package com.dwbh.backend.controller.notification;

import com.dwbh.backend.dto.chat.ChatDTO;
import com.dwbh.backend.dto.notification.NotificationDTO;
import com.dwbh.backend.service.notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
@Tag(name = "Notification API", description = "알림 API")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{notificationSeq}")
    @Operation(summary = "알림 상세 조회 (채팅방 입장)")
    public ResponseEntity<ChatDTO.Response> readNotification(@PathVariable Long notificationSeq) {
        ChatDTO.Response chatResponseDTO = notificationService.readNotification(notificationSeq);

        return ResponseEntity.ok(chatResponseDTO);
    }

    @GetMapping
    @Operation(summary = "알림 조회 (최근 2주)")
    public ResponseEntity<List<NotificationDTO>> readNotificationList() {
        List<NotificationDTO> notificationDTOList = notificationService.readNotificationList();

        return ResponseEntity.ok(notificationDTOList);
    }
}
