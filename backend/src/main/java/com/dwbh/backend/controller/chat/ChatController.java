package com.dwbh.backend.controller.chat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@Tag(name = "Chatting API", description = "채팅방 API")
public class ChatController  {

    public final ChatService chatService;

    @PostMapping("/{seq}/chat")
    @Operation(summary = "채팅 추가")
    public ResponseEntity<> createChat (@PathVariable Long seq) {

    }


}
