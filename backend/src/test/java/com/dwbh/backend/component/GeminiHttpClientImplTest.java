package com.dwbh.backend.component;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GeminiHttpClientImplTest {

    @Autowired
    ChatMessageComponent chatSuggestComponent;

    @Test
    void suggestTest() throws Exception {
        String message = chatSuggestComponent.chatMessageSuggest("남자들이 좋아하는 운동복 추천해줘", "1");

        System.out.println(message);
    }
}