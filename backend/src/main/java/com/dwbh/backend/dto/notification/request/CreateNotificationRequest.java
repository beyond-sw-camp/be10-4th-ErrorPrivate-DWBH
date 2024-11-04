package com.dwbh.backend.dto.notification.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateNotificationRequest {
    private Long chatSeq;
    private Long userSeq;
}
