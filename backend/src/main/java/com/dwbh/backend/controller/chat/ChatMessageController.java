package com.dwbh.backend.controller.chat;

import com.dwbh.backend.dto.chat.ChatMessageDTO;
import com.dwbh.backend.service.chat.ChatMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Chatting Message API", description = "채팅방 API")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    private final SimpMessagingTemplate template;

    // 채팅방 입장
    @MessageMapping("/chat/enter/{roomId}")
    @SendTo("/sub/chat/room/{roomId}")
    public void enterUser(@DestinationVariable("roomId") String roomId, @Payload ChatMessageDTO.Request message){
        message.changeMessageRequest("1", message.getSenderNickName() + "님이 채팅방에 입장하였습니다.", "ENTER");
        chatMessageService.saveMessage(message);
    }

    // 채팅방 대화
    @MessageMapping("/chat/talk/{roomId}")
    @SendTo("/sub/chat/room/{roomId}")
    public void talkUser(@DestinationVariable("roomId") String roomId, @Payload ChatMessageDTO.Request message) {
        chatMessageService.saveMessage(message);
    }

    // 채팅방 퇴장
    @MessageMapping("/chat/exit/{roomId}")
    @SendTo("/sub/chat/room/{roomId}")
    public void exitUser(@DestinationVariable("roomId") String roomId, @Payload ChatMessageDTO.Request message){
        message.changeMessageRequest("1", message.getSenderNickName() + "님이 채팅방에 입장하였습니다.", "EXIT");
        chatMessageService.saveMessage(message);
    }

}
