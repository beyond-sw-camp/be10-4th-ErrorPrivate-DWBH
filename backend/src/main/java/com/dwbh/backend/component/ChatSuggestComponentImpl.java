package com.dwbh.backend.component;

import com.dwbh.backend.dto.chat.ChatSuggestRequest;
import com.dwbh.backend.dto.chat.ChatSuggestResponse;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatSuggestComponentImpl {
    public static final String GEMINI_PRO = "gemini-pro";

    private final ChatSuggestComponent chatSuggestComponent;

    private ChatSuggestResponse httpRequest(ChatSuggestRequest request) {
        try {
            return chatSuggestComponent.httpRequest(GEMINI_PRO, request);
        } catch(Exception e) {
            log.error("httpRequest : ", e);
            return null;
        }
    }

    public String service(String text) {
        ChatSuggestRequest request = new ChatSuggestRequest(text, "user", new ArrayList<>());
        ChatSuggestResponse response = httpRequest(request);

        return response.getCandidates()
                .stream()
                .findFirst()
                .flatMap(candidate -> candidate.getContent().getParts()
                        .stream()
                        .findFirst()
                        .map(ChatSuggestResponse.TextPart::getText))
                .orElse(null);
    }
}
