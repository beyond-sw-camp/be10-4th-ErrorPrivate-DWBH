package com.dwbh.backend.service.chat;

import com.dwbh.backend.common.util.DateTimeUtil;
import com.dwbh.backend.dto.chat.ChatDTO;
import com.dwbh.backend.dto.chat.ChatMessageDTO;
import com.dwbh.backend.dto.notification.request.CreateNotificationRequest;
import com.dwbh.backend.entity.Chat;
import com.dwbh.backend.entity.Notification;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.mapper.ChatMapper;
import com.dwbh.backend.mapper.NotificationMapper;
import com.dwbh.backend.repository.chat.ChatRepository;
import com.dwbh.backend.repository.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final NotificationRepository notificationRepository;
    private final ChatMapper chatMapper;
    private final NotificationMapper notificationMapper;
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;

    @Transactional
    public boolean createChat(ChatDTO.Create chatCreateDTO) {
        boolean result = false;
        try {
            if (ObjectUtils.isEmpty(chatCreateDTO)) {
                log.error("createChat Error chatCreateDTO : {} ", chatCreateDTO);
                throw new CustomException(ErrorCodeType.CHAT_CREATE_ERROR);
            }

            Chat chat = chatMapper.toEntity(chatCreateDTO);
            chatRepository.save(chat);

            ChatDTO chatDTO = chatMapper.toDTO(chat);
            if(chatDTO.getChatSeq() == null) {
                throw new CustomException(ErrorCodeType.CHAT_CREATE_ERROR);
            }

            // 채팅방 생성 완료 시 알림 생성
            Notification notification = notificationMapper.toEntity(new CreateNotificationRequest(chatDTO.getChatSeq(), chatDTO.getReceiveSeq()));
            notificationRepository.save(notification);

            result = true;
        } catch (Exception e) {
            log.error("createChat Error : {}", e.getMessage());
        }

        return result;
    }

    public List<ChatDTO.Response> readChatList() {
        List<ChatDTO.Response> chatResponseList = null;

        try {

            // TODO 아영 - 유저 검증코드 완성되면 테스트 해보기
            List<Chat> chatList = chatRepository.findAll();
                    /*.stream().filter(chat -> chat.getSendUser().getUserSeq().toString().equals(AuthUtil.getAuthUser()))
                    .toList();*/

            chatResponseList = chatList.stream()
                    .map(chat -> modelMapper.map(chat, ChatDTO.Response.class))
                    .collect(Collectors.toList());

            // 마지막 메세지와 읽음여부 반환
            for (ChatDTO.Response response : chatResponseList) {
                checkChatLastMessage(response);
                checkEvaluationPeriod(response);
            }

        } catch (Exception e) {
            log.error("readChatList Error : {}", e.getMessage());
            throw new CustomException(ErrorCodeType.CHAT_NOT_FOUND);
        }

        return chatResponseList;
    }

    public void checkChatLastMessage(ChatDTO.Response response) {
        Query query = new Query(Criteria.where("chatRoomSeq").is(response.getChatSeq().toString()))
                .with(Sort.by(Sort.Direction.DESC, "regDate")).limit(1);

        ChatMessageDTO.Response messageResponse = mongoTemplate.findOne(query, ChatMessageDTO.Response.class, "request");
        if(!ObjectUtils.isEmpty(messageResponse)) {
            response.setLastMessage(messageResponse.getMessage());
            response.setReadYn(messageResponse.getReadYn());
        } else {
            response.setLastMessage(" ");
        }
    }

    public void checkEvaluationPeriod(ChatDTO.Response response) {
        if (response.getEndDate() != null) {
            response.setShowEvaluation(DateTimeUtil.isBeforeWeek(response.getEndDate(), 2));
        }
    }

}
