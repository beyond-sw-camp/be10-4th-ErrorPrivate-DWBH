package com.dwbh.backend.service.notification;

import com.dwbh.backend.dto.notification.request.CreateNotificationRequest;
import com.dwbh.backend.entity.Chat;
import com.dwbh.backend.entity.Notification;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.repository.chat.ChatRepository;
import com.dwbh.backend.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ChatRepository chatRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public void createNotification(Long chatSeq) {
        Chat chat = chatRepository.findById(chatSeq).orElseThrow(() -> new CustomException(ErrorCodeType.CHAT_NOT_FOUND));

        Notification notification = modelMapper.map(new CreateNotificationRequest(chat.getChatSeq(), chat.getReceiveSeq()), Notification.class);

        notificationRepository.save(notification);
    }
}
