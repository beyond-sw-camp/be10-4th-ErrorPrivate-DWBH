package com.dwbh.backend.component;

import com.dwbh.backend.dto.chat.ChatSuggestRequest;
import com.dwbh.backend.dto.chat.ChatSuggestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatSuggestComponentImpl {
    public static final String GEMINI_PRO = "gemini-pro";

    private final ChatSuggestComponent chatSuggestComponent;

    private ChatSuggestResponse httpRequest(ChatSuggestRequest request) {
        return chatSuggestComponent.httpRequest(GEMINI_PRO, request);
    }

    public String httpRequest(String text) {
        ChatSuggestRequest request = new ChatSuggestRequest(text);
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
