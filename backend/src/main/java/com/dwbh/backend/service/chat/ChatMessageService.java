package com.dwbh.backend.service.chat;

import com.dwbh.backend.dto.chat.ChatMessageDTO;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j @Service
@RequiredArgsConstructor
public class ChatMessageService {


    private final MongoTemplate mongoTemplate;

    public List<ChatMessageDTO.Response> getChatHistory(String roomId) {
        Query query = new Query(Criteria.where("chatRoomSeq").is(roomId));

        return mongoTemplate.find(query, ChatMessageDTO.Response.class, "request");
    }

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
