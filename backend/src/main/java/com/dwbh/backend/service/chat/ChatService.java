package com.dwbh.backend.service.chat;

import com.dwbh.backend.common.entity.YnType;
import com.dwbh.backend.common.util.AuthUtil;
import com.dwbh.backend.common.util.DateTimeUtil;
import com.dwbh.backend.dto.chat.ChatDTO;
import com.dwbh.backend.dto.chat.ChatMessageDTO;
import com.dwbh.backend.dto.chat.ChatSuggestRequest;
import com.dwbh.backend.dto.chat.suggest.ChatMessageSuggest;
import com.dwbh.backend.dto.notification.request.CreateNotificationRequest;
import com.dwbh.backend.entity.Chat;
import com.dwbh.backend.entity.Notification;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.mapper.ChatMapper;
import com.dwbh.backend.mapper.NotificationMapper;
import com.dwbh.backend.repository.chat.ChatMessageSuggestRepository;
import com.dwbh.backend.repository.chat.ChatRepository;
import com.dwbh.backend.repository.counsel_offer.CounselOfferRepository;
import com.dwbh.backend.repository.notification.NotificationRepository;
import com.dwbh.backend.service.UserService;
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

import java.util.ArrayList;
import java.util.List;

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
    private final ChatMessageSuggestRepository chatMessageSuggestRepository;
    private final CounselOfferRepository counselOfferRepository;
    private final UserService userService;

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

            // 채팅방 생성 완료 시 채팅 프롬프트 생성
            String counselorContent = counselOfferRepository.findCounselorContentByCounselOfferSeq(chatCreateDTO.getCounselOfferSeq()) + " 대답은 항상 30글자 이내 5줄 이내로 작성해주세요.";
            chatMessageSuggestRepository.save(new ChatMessageSuggest(chatDTO.getChatSeq().toString(), new ChatSuggestRequest(counselorContent, "user", new ArrayList<>()).getContents()));

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
        Long userSeq = userService.getUserSeq(AuthUtil.getAuthUser());
        List<ChatDTO.Response> chatList;
        try {
             chatList = chatRepository.findByUserSeq(userSeq);

            // 마지막 메세지와 읽음 여부 반환
            for (ChatDTO.Response response : chatList) {
                checkChatLastMessage(response);
                checkEvaluationPeriod(response);
            }

        } catch (Exception e) {
            log.error("readChatList Error : {}", e.getMessage());
            throw new CustomException(ErrorCodeType.CHAT_NOT_FOUND);
        }

        return chatList;
    }

    public void checkChatLastMessage(ChatDTO.Response response) {
        Query query = new Query(Criteria.where("chatRoomSeq").is(response.getChatSeq().toString())
                .and("type").is("TALK"))
                .with(Sort.by(Sort.Direction.DESC, "regDate")).limit(1);

        ChatMessageDTO.Response messageResponse = mongoTemplate.findOne(query, ChatMessageDTO.Response.class, "request");
        if(!ObjectUtils.isEmpty(messageResponse)) {
            response.setLastMessage(messageResponse.getMessage()==null ? "" : messageResponse.getMessage());
            response.setReadYn("N".equals(messageResponse.getReadYn()) ? YnType.N : YnType.Y);
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
