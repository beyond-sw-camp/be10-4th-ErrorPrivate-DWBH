package com.dwbh.backend.service.notification;

import com.dwbh.backend.dto.chat.ChatDTO;
import com.dwbh.backend.dto.notification.NotificationDTO;
import com.dwbh.backend.entity.Chat;
import com.dwbh.backend.entity.Notification;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.mapper.NotificationMapper;
import com.dwbh.backend.repository.chat.ChatRepository;
import com.dwbh.backend.repository.notification.NotificationRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Tag(name = "Notification API", description = "알림 API")
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ChatRepository chatRepository;
    private final ModelMapper modelMapper;
    private final NotificationMapper notificationMapper;

    @Transactional
    public ChatDTO.Response readNotification(Long notificationSeq) {
        // 알림 읽음 여부 업데이트
        Notification notification = notificationRepository.findById(notificationSeq).orElseThrow(()-> new CustomException(ErrorCodeType.NOTICE_NOT_FOUND));
        notification.updateNotificationCheckYn();

        Long chatSeq = notification.getChat().getChatSeq();
        Chat chat = chatRepository.findById(chatSeq).orElseThrow(() -> new CustomException(ErrorCodeType.CHAT_NOT_FOUND));

        ChatDTO.Response chatResponseDTO = modelMapper.map(chat, ChatDTO.Response.class);

        log.info("chatResponseDTO : {}", chatResponseDTO);
        return chatResponseDTO;
    }

    public List<NotificationDTO> readNotificationList() {
        // 토큰에서 가져올 임시 userSeq 값
        Long userSeq = 1L;
        List<Notification> notificationList = notificationRepository.findByUserSeq(userSeq);

        List<NotificationDTO> notificationDTOList = notificationList.stream()
                .map(notificationMapper::toDTO)
                .toList();

        log.info("notificationDTOList : {}", notificationDTOList);
        return notificationDTOList;
    }
}
