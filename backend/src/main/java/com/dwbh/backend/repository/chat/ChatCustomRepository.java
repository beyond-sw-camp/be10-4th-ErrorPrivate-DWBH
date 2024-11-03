package com.dwbh.backend.repository.chat;

import com.dwbh.backend.entity.Chat;

public interface ChatCustomRepository {
    Chat findByChatSeqAndNotificationSeq(Long chatSeq, Long notificationSeq);
}
