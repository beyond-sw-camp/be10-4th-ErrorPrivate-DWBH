package com.dwbh.backend.service.chat;

import com.dwbh.backend.dto.chat.ChatMessageDTO;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.repository.chat.ChatMessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Slf4j @Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final MongoTemplate mongoTemplate;


    @Transactional
    public void saveMessage(ChatMessageDTO.Request request) {
        try {

             mongoTemplate.insert(request);

        } catch (Exception e) {
            log.error("saveMessage Error : {}", e.getMessage());
            throw new CustomException(ErrorCodeType.CHAT_NOT_FOUND);
        }
    }

}
