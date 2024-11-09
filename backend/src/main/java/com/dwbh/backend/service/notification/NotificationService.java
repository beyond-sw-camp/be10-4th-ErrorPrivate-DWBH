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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Tag(name = "Notification API", description = "알림 API")
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ChatRepository chatRepository;
    private final ModelMapper modelMapper;
    private final NotificationMapper notificationMapper;

    // 사용자별 SSE 연결을 관리하는 맵
    private final Map<Long, SseEmitter> clients = new ConcurrentHashMap<>();

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

        // 채팅 상대방 닉네임 찾기 (게시글 작성자이자 채팅방 생성자)
        List<String> userNicknameList = notificationList.stream()
                .map(notification -> notification.getChat().getReceiveUser().getUserNickname())  // Notification 객체에서 userNickname 추출
                .toList();

        // 알림 체크를 다 했는지 체크
        Boolean isConfirmation = notificationDTOList.stream()
                .noneMatch(notification -> notification.getNotificationCheckYn() == YnType.N);

        NotificationResponse notificationResponse = NotificationResponse.builder()
                .notifications(notificationDTOList)
                .userNicknames(userNicknameList)
                .isConfirmation(isConfirmation)
                .build();

        // SSE 로 알림 발송
        SseEmitter emitter = clients.get(userSeq);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(notificationResponse));
            } catch (IOException e) {
                clients.remove(userSeq);
            }
        }

        return notificationResponse;
    }

    // SSE 연결
    public SseEmitter createEmitter(Long userSeq) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        try {
            emitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 새로운 클라이언트를 맵에 저장
        clients.put(userSeq, emitter);

        // SSE 연결이 완료되거나 중단되면 맵에서 제거
        emitter.onCompletion(() -> clients.remove(userSeq));
        emitter.onTimeout(() -> clients.remove(userSeq));
        emitter.onError((e) -> clients.remove(userSeq));

        return emitter;
    }
}
