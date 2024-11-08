package com.dwbh.backend.dto.chat;

import com.dwbh.backend.dto.chat.suggest.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
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
    public ChatSuggestRequest(String text, String role, List<Content> contents) {
        Part part = new TextPart(text);
        Content content = new Content(Collections.singletonList(part), role);
        if(contents.isEmpty()) {
            this.contents = List.of(content);
        } else {
            contents.add(content);
            this.contents = contents;
        }
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
    public ChatSuggestRequest(String text, InlineData inlineData, String role, List<Content> contents) {
        Content content = new Content(
                List.of(
                        new TextPart(text),
                        new InlineDataPart(inlineData)
                ), role
        );
        if(contents.isEmpty()) {
            this.contents = List.of(
                content
            );
        } else {
            contents.add(content);

            this.contents = contents;
        }
    }
}
