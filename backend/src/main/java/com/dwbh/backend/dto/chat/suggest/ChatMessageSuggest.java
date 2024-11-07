package com.dwbh.backend.dto.chat.suggest;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "chat_message_suggest")
public class ChatMessageSuggest {
    @Id
    private String id;  // MongoDB의 ID 필드
    @Field("chatRoomSeq")
    private Integer chatRoomSeq;
    private List<Content> contents;

    public ChatMessageSuggest(Integer chatRoomSeq, List<Content> contents) {
        this.chatRoomSeq = chatRoomSeq;
        this.contents = contents;
    }
}
