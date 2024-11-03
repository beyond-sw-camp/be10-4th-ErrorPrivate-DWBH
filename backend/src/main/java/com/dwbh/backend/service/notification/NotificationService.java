package com.dwbh.backend.service.notification;

import com.dwbh.backend.dto.chat.ChatDTO;
import com.dwbh.backend.dto.notification.request.CreateNotificationRequest;
import com.dwbh.backend.dto.notification.response.NotificationResponse;
import com.dwbh.backend.entity.Chat;
import com.dwbh.backend.entity.Notification;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.repository.chat.ChatRepository;
import com.dwbh.backend.repository.notification.NotificationRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Tag(name = "Notification API", description = "알림 API")
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ChatRepository chatRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public boolean createNotification(Long chatSeq) {
        try {
            Chat chat = chatRepository.findById(chatSeq).orElseThrow(() -> new CustomException(ErrorCodeType.CHAT_NOT_FOUND));

            Notification notification = modelMapper.map(new CreateNotificationRequest(chat.getChatSeq(), chat.getReceiveSeq()), Notification.class);

            notificationRepository.save(notification);

            return true;
        } catch (Exception e) {
            log.error("NotificationService createNotification error : {}", e.getMessage());

            return false;
        }
    }

    @Transactional
    public ChatDTO.ChatResponseDTO readNotification(Long chatSeq, Long notificationSeq) {
        Chat chat = chatRepository.findByChatSeqAndNotificationSeq(chatSeq, notificationSeq);
        // 알림 읽음 여부 업데이트
        Notification notification = notificationRepository.findById(notificationSeq).orElseThrow(()-> new CustomException(ErrorCodeType.NOTICE_NOT_FOUND));
        notification.updateNotificationCheckYn();

        ChatDTO.ChatResponseDTO chatResponseDTO = modelMapper.map(chat, ChatDTO.ChatResponseDTO.class);

        log.info("chatResponseDTO : {}", chatResponseDTO);
        return chatResponseDTO;
    }

    public List<NotificationResponse> readNotificationList() {
        // 토큰에서 가져올 임시 userSeq 값
        Long userSeq = 1L;
        List<Notification> notificationList = notificationRepository.findByUserSeq(userSeq);

        List<NotificationResponse> notificationResponseList = notificationList.stream()
                .map(m -> modelMapper.map(m, NotificationResponse.class))
                .toList();

        log.info("notificationResponseList : {}", notificationResponseList);
        return notificationResponseList;
    }
}
