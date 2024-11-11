package com.dwbh.backend.service.chat;


import com.dwbh.backend.component.ChatMessageComponent;
import com.dwbh.backend.common.util.DateTimeUtil;
import com.dwbh.backend.dto.chat.ChatDTO;
import com.dwbh.backend.dto.chat.ChatMessageDTO;
import com.dwbh.backend.entity.Chat;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.repository.chat.ChatMessageRepository;
import com.dwbh.backend.repository.chat.ChatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageComponent chatMessageComponent;
    private final ChatMessageRepository chatMessageRepository;

    private final MongoTemplate mongoTemplate;

    private final ChatRepository chatRepository;

    private final ModelMapper modelMapper;

    public List<ChatMessageDTO.Response> getChatHistory(String roomId) {
        List<ChatMessageDTO.Response> responseList = new ArrayList<>();;

        // 입장 알림 조회
        Query queryLimit = new Query(Criteria.where("chatRoomSeq").is(roomId).and("type").is("ENTER"))
                .with(Sort.by(Sort.Direction.ASC, "timestamp"))
                .skip(1).limit(1);
        ChatMessageDTO.Response enterResponse = mongoTemplate.findOne(queryLimit, ChatMessageDTO.Response.class, "request");
        if (!ObjectUtils.isEmpty(enterResponse)) {
            responseList.add(enterResponse);
        }

        // 대화 내역 조회
        Query query = new Query(Criteria.where("chatRoomSeq").is(roomId).and("type").is("TALK"))
                .with(Sort.by(Sort.Direction.ASC, "timestamp"));
        List<ChatMessageDTO.Response> talkResponses = mongoTemplate.find(query, ChatMessageDTO.Response.class, "request");
        responseList.addAll(talkResponses);

        return responseList;
    }

    @Transactional
    public ChatMessageDTO.Response saveMessage(ChatMessageDTO.Request request) {
        ChatMessageDTO.Response response;
        try {

            mongoTemplate.insert(request);
            if(!request.getType().equals("EXIT") && !request.getType().equals("ENTER")) {
                chatMessageComponent.chatMessageSuggest(request.getMessage(), request.getChatRoomSeq());
            }

             if("EXIT".equals(request.getType())) {
                 ChatDTO.Update update = ChatDTO.Update.builder()
                         .chatSeq(Long.valueOf(request.getChatRoomSeq()))
                         .endDate(LocalDateTime.now())
                         .build();
                 chatRepository.save(modelMapper.map(update, Chat.class));
             }

            Query queryLimit = new Query(Criteria.where("chatMessageSeq").is(request.getChatMessageSeq()));
            response = mongoTemplate.findOne(queryLimit, ChatMessageDTO.Response.class, "request");

        } catch (Exception e) {
            log.error("saveMessage Error : {}", e.getMessage());
            throw new CustomException(ErrorCodeType.CHAT_NOT_FOUND);
        }

        return response;
    }

    public void endChatRoom(ChatDTO.Update update) {
        Chat existingChat = chatRepository.findById(update.getChatSeq())
                .orElseThrow(() -> new CustomException(ErrorCodeType.CHAT_NOT_FOUND));
        modelMapper.map(update, existingChat);
        chatRepository.save(existingChat);
    }


}
