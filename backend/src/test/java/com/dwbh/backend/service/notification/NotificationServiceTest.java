package com.dwbh.backend.service.notification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    /* 알림 상세 조회 테스트 */
    @Test
    void testReadNotification() {
        Assertions.assertDoesNotThrow(
                () -> notificationService.readNotification(3L)
        );
    }

    /* 알림 리스트 조회(2주 데이터) 테스트 */
    @Test
    void testReadNotificationList() {
        Assertions.assertDoesNotThrow(
                () -> notificationService.readNotificationList(3L)
        );
    }
}