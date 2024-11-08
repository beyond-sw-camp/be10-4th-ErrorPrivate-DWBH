package com.dwbh.backend.service.chat;

import com.dwbh.backend.dto.chat.ChatMessageDTO;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j @Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageComponent chatMessageComponent;
    private final ChatMessageRepository chatMessageRepository;

    private final MongoTemplate mongoTemplate;


    @Transactional
    public boolean createChatMessage(ChatMessageDTO chatMessageDTO) {
        boolean result = false;
        try {
            //chatMessageDTO.setMessageType(ChatMessageDTO.MessageType.TALK);

            mongoTemplate.insert(request);
            chatMessageComponent.chatMessageSuggest(chatMessageDTO.getMessage(), chatMessageDTO.getChatRoomSeq());
            //채팅 메세지 몽고디비 연동
            result = true;

        } catch (Exception e) {
            log.error("saveMessage Error : {}", e.getMessage());
            throw new CustomException(ErrorCodeType.CHAT_NOT_FOUND);
        }
    }

}
