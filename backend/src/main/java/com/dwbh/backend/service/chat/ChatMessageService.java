package com.dwbh.backend.service.chat;


import com.dwbh.backend.component.ChatMessageComponent;
import com.dwbh.backend.dto.chat.ChatMessageDTO;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.repository.chat.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageComponent chatMessageComponent;
    private final ChatMessageRepository chatMessageRepository;

    private final MongoTemplate mongoTemplate;


    @Transactional
    public void saveMessage(ChatMessageDTO.Request request) {
        boolean result = false;
        try {
            //chatMessageDTO.setMessageType(ChatMessageDTO.MessageType.TALK);

            mongoTemplate.insert(request);
            chatMessageComponent.chatMessageSuggest(request.getMessage(), request.getChatRoomSeq());
            //채팅 메세지 몽고디비 연동

        } catch (Exception e) {
            log.error("saveMessage Error : {}", e.getMessage());
            throw new CustomException(ErrorCodeType.CHAT_NOT_FOUND);
        }
    }

}
