package com.dwbh.backend.controller.chat;

import com.dwbh.backend.dto.chat.ChatDTO;
import com.dwbh.backend.dto.chat.ChatRoomDTO;
import com.dwbh.backend.service.chat.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/chat")
@Tag(name = "Chatting API", description = "채팅 목록 API")
public class ChatController  {

    public final ChatService chatService;

    @PostMapping
    @Operation(summary = "채팅 추가")
    public ResponseEntity<Boolean> createChat (@RequestBody ChatDTO.Create chatCreateDTO) {
        boolean result = chatService.createChat(chatCreateDTO);

        return ResponseEntity.ok(result);
    }

    @GetMapping
    @Operation(summary = "채팅 목록 조회")
    public List<ChatDTO.Response> readChatList () {
        return chatService.readChatList();
    }

    @GetMapping("/room")
    @Operation(summary = "채팅방 조회")
    public ChatRoomDTO readChatRoom (String roomId) {
        return chatService.readChatRoom(roomId);
    }


}
