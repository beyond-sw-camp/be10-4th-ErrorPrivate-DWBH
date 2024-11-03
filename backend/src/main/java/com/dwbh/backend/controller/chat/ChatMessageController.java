package com.dwbh.backend.controller.chat;

import com.dwbh.backend.dto.chat.ChatMessageDTO;
import com.dwbh.backend.service.chat.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/chat/message")
@Tag(name = "Chatting API", description = "채팅방 API")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @PostMapping
    @Operation(summary = "채팅 메세지 추가")
    public String createChatMessage (ChatMessageDTO chatMessageDTO) {
        boolean result = chatMessageService.createChatMessage(chatMessageDTO);

        return result ? "성공" : "실패";
    }

    // 새로운 사용자가 웹 소켓을 연결할 때 실행됨
    // @EventListener은 한개의 매개변수만 가질 수 있다.
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        log.info("Received a new web socket connection");
    }

    // 사용자가 웹 소켓 연결을 끊으면 실행됨
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccesor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccesor.getSessionId();

        log.info("sessionId Disconnected : " + sessionId);
    }

    // /pub/cache 로 메시지를 발행한다.
    @MessageMapping("/cache")
    @SendTo("/sub/cache")
    public void sendMessage(Map<String, Object> params) {

        // /sub/cache 에 구독중인 client에 메세지를 보낸다.
        simpMessageSendingOperations.convertAndSend("/sub/cache/" + params.get("channelId"), params);
    }


}
