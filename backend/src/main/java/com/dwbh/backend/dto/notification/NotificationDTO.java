package com.dwbh.backend.dto.notification;

import com.dwbh.backend.common.entity.YnType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDTO {
    private Long notificationSeq;
    private Long chatSeq;
    private Long userSeq;

    private YnType notificationCheckYn;
    private String notificationRegDate;
    private String notificationModDate;
}
