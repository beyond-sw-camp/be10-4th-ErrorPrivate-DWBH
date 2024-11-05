package com.dwbh.backend.dto.chat;

import lombok.*;

@Getter @Setter
public class ChatMessageDTO {

    private String roomId;
    private String writer;
    private String message;

}
