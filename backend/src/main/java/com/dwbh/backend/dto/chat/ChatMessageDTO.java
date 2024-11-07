package com.dwbh.backend.dto.chat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@ToString
public class ChatMessageDTO {

    private Long seq;
    private Long chatRoomSeq;
    private Long memberSeq;

    private String message;
    private String region;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime regDate;

}
