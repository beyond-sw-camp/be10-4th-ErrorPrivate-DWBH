package com.dwbh.backend.component;

import com.dwbh.backend.dto.chat.ChatSuggestRequest;
import com.dwbh.backend.dto.chat.ChatSuggestResponse;
import com.dwbh.backend.dto.chat.suggest.ChatMessageSuggest;
import com.dwbh.backend.repository.chat.ChatMessageSuggestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatMessageComponent {
    public static final String GEMINI_PRO = "gemini-pro";

    private final GeminiHttpClient geminiHttpClient;
    private final ChatMessageSuggestRepository chatMessageSuggestRepository;

    private ChatSuggestResponse httpRequest(ChatSuggestRequest request) {
        try {
            return geminiHttpClient.httpRequest(GEMINI_PRO, request);
        } catch(Exception e) {
            log.error("httpRequest : ", e);
            return null;
        }
    }

    public String chatMessageSuggest(String text, String chatRoomSeq) throws Exception {
        try {
            ChatMessageSuggest chatMessageSuggest = chatMessageSuggestRepository.findByChatRoomSeq(chatRoomSeq);
            log.info("chatMessageSuggest : {}", chatMessageSuggest);
            ChatSuggestRequest request = new ChatSuggestRequest(text, "user", chatMessageSuggest.getContents());

            chatMessageSuggestRepository.save(new ChatMessageSuggest(chatRoomSeq, request.getContents()));

            ChatSuggestResponse response = httpRequest(request);

            String suggestMessage = response.getCandidates()
                    .stream()
                    .findFirst()
                    .flatMap(candidate -> candidate.getContent().getParts()
                            .stream()
                            .findFirst()
                            .map(ChatSuggestResponse.TextPart::getText))
                    .orElseThrow();

            return suggestMessage;
        } catch (Exception e) {
            log.error("Exception : ", e);
            return null;
        }

    }
}
