package com.dwbh.backend.dto.notification.response;

import com.dwbh.backend.common.entity.YnType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationResponse {
    private Long notificationSeq;
    private Long chatSeq;
    private YnType notificationCheckYn;
    private String notificationRegDate;

    private String notificationComment;
}
