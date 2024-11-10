package com.dwbh.backend.service.notification;

import com.dwbh.backend.common.entity.YnType;
import com.dwbh.backend.dto.chat.ChatDTO;
import com.dwbh.backend.dto.notification.NotificationDTO;
import com.dwbh.backend.dto.notification.NotificationResponse;
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
        Notification notification = notificationRepository.findById(notificationSeq).orElseThrow(() -> new CustomException(ErrorCodeType.NOTICE_NOT_FOUND));
        notification.updateNotificationCheckYn();

        Long chatSeq = notification.getChat().getChatSeq();
        Chat chat = chatRepository.findById(chatSeq).orElseThrow(() -> new CustomException(ErrorCodeType.CHAT_NOT_FOUND));

        ChatDTO.Response chatResponseDTO = modelMapper.map(chat, ChatDTO.Response.class);

        log.info("chatResponseDTO : {}", chatResponseDTO);
        return chatResponseDTO;
    }

    // 알림 발송 및 전체 알림 조회
    public NotificationResponse readNotificationList(Long userSeq) {
        List<Notification> notificationList = notificationRepository.findByUserSeq(userSeq);

        List<NotificationDTO> notificationDTOList = notificationList.stream()
                .map(notificationMapper::toDTO)
                .toList();

        // 각 Notification 객체에서 userNickname을 추출하여 개별 NotificationDTO에 설정
        for (int i = 0; i < notificationDTOList.size(); i++) {
            String userNickname = notificationList.get(i).getChat().getReceiveUser().getUserNickname();
            notificationDTOList.get(i).setUserNickname(userNickname);
        }

        // 알림 체크를 다 했는지 체크
        Boolean isConfirmation = notificationDTOList.stream()
                .noneMatch(notification -> notification.getNotificationCheckYn() == YnType.N);

        NotificationResponse notificationResponse = NotificationResponse.builder()
                .notifications(notificationDTOList)
                .isConfirmation(isConfirmation)
                .build();

        return notificationResponse;
    }

}
