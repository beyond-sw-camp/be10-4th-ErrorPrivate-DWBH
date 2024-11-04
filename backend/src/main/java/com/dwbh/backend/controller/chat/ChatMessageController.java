package com.dwbh.backend.controller.chat;

import com.dwbh.backend.dto.chat.ChatMessageDTO;
import com.dwbh.backend.service.chat.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/chat/message")
@Tag(name = "Chatting API", description = "채팅방 API")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @GetMapping
    @Operation(summary = "채팅 메세지 전송")
    public String createChatMessage (ChatMessageDTO chatMessageDTO) {
        log.info("createChatMessage 진입 성공");
        boolean result = chatMessageService.createChatMessage(chatMessageDTO);

        return result ? "성공" : "실패";
    }

}
