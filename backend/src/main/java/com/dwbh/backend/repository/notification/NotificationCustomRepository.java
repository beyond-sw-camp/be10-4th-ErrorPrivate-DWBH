package com.dwbh.backend.repository.notification;

import com.dwbh.backend.entity.Notification;

import java.util.List;

public interface NotificationCustomRepository {
    List<Notification> findByUserSeq(Long userSeq);
}
