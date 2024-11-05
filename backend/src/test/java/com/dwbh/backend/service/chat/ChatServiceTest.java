package com.dwbh.backend.service.chat;

import com.dwbh.backend.dto.chat.ChatDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    private static Stream<Arguments> createChatArguments() {
        return Stream.of(
                Arguments.of(
                        2L,
                        1L,
                        2L
                )
        );
    }

    /* 채팅방 추가 테스트 */
    @ParameterizedTest
    @MethodSource("createChatArguments")
    void testCreateChat(
            Long counselOfferSeq, Long sendSeq, Long receiveSeq
    ) {

        ChatDTO.ChatRequestDTO chatRequestDTO = new ChatDTO.ChatRequestDTO(
                counselOfferSeq,
                sendSeq,
                receiveSeq
        );

        Assertions.assertDoesNotThrow(
                () -> chatService.createChat(chatRequestDTO)
        );
    }

    /* 채팅방 목록 조회 테스트 */
    @Test
    void testReadChatList() {
        Assertions.assertDoesNotThrow(
                () -> chatService.readChatList()
        );
    }

}