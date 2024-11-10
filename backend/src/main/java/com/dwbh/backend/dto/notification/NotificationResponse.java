package com.dwbh.backend.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "알림 응답 데이터")
public class NotificationResponse {
    List<NotificationDTO> notifications;
    Boolean isConfirmation;
}
