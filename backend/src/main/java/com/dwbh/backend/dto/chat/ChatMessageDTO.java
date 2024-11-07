package com.dwbh.backend.dto.chat;

import lombok.*;

@Getter @Setter
@ToString
public class ChatMessageDTO {

    private Long chatRoomSeq;
    private String writer;
    private String message;

}
