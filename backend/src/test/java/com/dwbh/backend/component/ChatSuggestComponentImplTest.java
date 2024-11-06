package com.dwbh.backend.component;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChatSuggestComponentImplTest {

    @Autowired
    ChatSuggestComponentImpl chatSuggestComponent;

    @Test
    void suggestTest() {
        String message = chatSuggestComponent.httpRequest("남자들이 좋아하는 운동복 추천해줘");

        System.out.println(message);
    }
}