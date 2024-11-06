package com.dwbh.backend.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

public class ChatSuggestRequest {

    private List<Content> contents;

    /*
    * 첫 채팅 - 채팅 메시지만 전달될 경우
    *
    * {
    * "contents" : [
    *         {
    *             "role": "user",
    *             "parts":[
    *                 {"text": "자기 반성과 소통 개선을 통해 인간관계 스트레스를 줄일 수 있습니다."}
    *             ]
    *         }
    *     ]
    * }
    * */
    public ChatSuggestRequest(String text) {
        Part part = new TextPart(text);
        Content content = new Content(Collections.singletonList(part));
        this.contents = List.of(content);
    }

    /*
    * 첫 채팅 - 이미지와 텍스트가 동시에 전달될 경우
    *
    * {
    *     "contents": [
    *         {
    *             "parts": [
    *                 {
    *                     "text": "What is this picture?"
    *                 },
    *                 {
    *                     "inline_data": {
    *                         "mime_type": "image/jpeg",
    *                         "data": "'$(base64 -w0 image.jpg)'"
    *                     }
    *                 }
    *             ]
    *         }
    *     ]
    * }
    * */
    public ChatSuggestRequest(String text, InlineData inlineData) {

        this.contents = List.of(
                new Content(
                        List.of(
                                new TextPart(text),
                                new InlineDataPart(inlineData)
                        )
                )
        );
    }

    @Getter
    @AllArgsConstructor
    private static class Content {
        private List<Part> parts;
    }

    @Getter
    @AllArgsConstructor
    private static class TextPart implements Part {
        public String text;
    }

    @Getter
    @AllArgsConstructor
    private static class InlineDataPart implements Part {
        public InlineData inlineData;
    }

    @Getter
    @AllArgsConstructor
    public static class InlineData {
        private String mimeType;
        private String data;
    }
    interface Part {}
}
