package com.dwbh.backend.service.chat;

import com.dwbh.backend.dto.chat.ChatMessageDTO;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

@Slf4j @Service
@RequiredArgsConstructor
public class ChatMessageService {


    @Transactional
    public boolean createChatMessage(ChatMessageDTO chatMessageDTO) {
        boolean result = false;
        try {
            chatMessageDTO.setMessageType(ChatMessageDTO.MessageType.TALK);

            //채팅 메세지 몽고디비 연동
            result = true;

        } catch (Exception e) {
            log.error("createChat Error : {}", e.getMessage());
            throw new CustomException(ErrorCodeType.CHAT_NOT_FOUND);
        }

        return result;
    }

}
