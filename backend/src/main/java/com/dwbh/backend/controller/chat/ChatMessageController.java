package com.dwbh.backend.controller.chat;

import com.dwbh.backend.dto.chat.ChatMessageDTO;
import com.dwbh.backend.service.chat.ChatMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Chatting Message API", description = "채팅방 API")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    private final SimpMessagingTemplate template;

    @MessageMapping(value = "/chat/enter")
    public void createChatMessage(ChatMessageDTO message){

        log.info("createChatMessage 진입 성공");
        //boolean result = chatMessageService.createChatMessage(chatMessageDTO);

        message.setMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getChatRoomSeq(), message);
    }

    @MessageMapping(value = "/chat/message")
    public void sendMessage(ChatMessageDTO message){

        log.info("sendMessage 진입 성공 , {}", message  );
        chatMessageService.createChatMessage(message);

        template.convertAndSend("/sub/chat/room/" + message.getChatRoomSeq(), message);
    }

}
