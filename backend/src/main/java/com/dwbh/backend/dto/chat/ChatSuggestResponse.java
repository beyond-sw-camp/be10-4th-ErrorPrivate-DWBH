package com.dwbh.backend.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatSuggestResponse {
    private List<Candidate> candidates;
    private UsageMetadata usageMetadata;
    private String modelVersion;

    @Getter
    public static class Candidate {
        private Content content;
    }

    @Getter
    public static class Content {
        private List<TextPart> parts;
        private String role;
    }

    @Getter
    public static class TextPart {
        private String text;
    }

    @Getter
    public static class SafetyRating {
        private String category;
        private String probability;
    }

    public static class UsageMetadata {
        private String promptTokenCount;
        private String candidatesTokenCount;
        private String totalTokenCount;
    }
    /*
    * 응답 데이터
    *
    * {
    *     "candidates": [
    *     {
    *         "content": {
    *         "parts": [
    *         {
    *             "text": "죄송합니다. 감정에 공감하지 못한 것 같네요. 인간관계의 스트레스를 줄이는 데 도움이 되는 몇 가지 친절한 조언을 드릴까요?"
    *         }
    *             ],
    *         "role": "model"
    *     },
    *         "finishReason": "STOP",
    *             "index": 0,
    *             "safetyRatings": [
    *         {
    *             "category": "HARM_CATEGORY_SEXUALLY_EXPLICIT",
    *                 "probability": "NEGLIGIBLE"
    *         },
    *         {
    *             "category": "HARM_CATEGORY_HATE_SPEECH",
    *                 "probability": "NEGLIGIBLE"
    *         },
    *         {
    *             "category": "HARM_CATEGORY_HARASSMENT",
    *                 "probability": "NEGLIGIBLE"
    *         },
    *         {
    *             "category": "HARM_CATEGORY_DANGEROUS_CONTENT",
    *                 "probability": "NEGLIGIBLE"
    *         }
    *         ]
    *     }
    * ],
    *     "usageMetadata": {
    *     "promptTokenCount": 229,
    *             "candidatesTokenCount": 48,
    *             "totalTokenCount": 277
    * },
    *     "modelVersion": "gemini-pro"
    * }
    *
    * */
}
