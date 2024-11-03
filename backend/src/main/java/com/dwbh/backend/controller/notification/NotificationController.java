package com.dwbh.backend.controller.notification;

import com.dwbh.backend.dto.chat.ChatDTO;
import com.dwbh.backend.dto.notification.response.NotificationResponse;
import com.dwbh.backend.service.notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat/{chatSeq}/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "알림 추가")
    public ResponseEntity<Boolean> createNotification(@PathVariable Long chatSeq) {

        boolean isSuccess = notificationService.createNotification(chatSeq);

        return ResponseEntity.ok(isSuccess);
    }

    @GetMapping("/{notificationSeq}")
    @Operation(summary = "알림 상세 조회 (채팅방 입장)")
    public ResponseEntity<ChatDTO.ChatResponseDTO> readNotification(@PathVariable Long chatSeq, @PathVariable Long notificationSeq) {
        ChatDTO.ChatResponseDTO chatResponseDTO = notificationService.readNotification(chatSeq, notificationSeq);

        return ResponseEntity.ok(chatResponseDTO);
    }

    @GetMapping
    @Operation(summary = "알림 조회 (최근 2주)")
    public ResponseEntity<List<NotificationResponse>> readNotificationList() {
        List<NotificationResponse> notificationList = notificationService.readNotificationList();

        return ResponseEntity.ok(notificationList);
    }

}
