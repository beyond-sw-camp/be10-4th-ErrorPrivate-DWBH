package com.dwbh.backend.dto.chat.suggest;

import jakarta.persistence.Id;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "chat_message_suggest")
public class ChatMessageSuggest {
    private Long chatRoomSeq;
    private List<Content> contents;
}
