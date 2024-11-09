package com.dwbh.backend.dto.chat;

import com.dwbh.backend.common.util.CustomLocalDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ChatMessageDTO {

    @Getter
    public static class Request {

        private String chatMessageSeq;
        private String chatRoomSeq;
        private String type;
        private String message;
        private String sendSeq;
        private String senderNickName;
        private String receiveSeq;
        @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
        private LocalDateTime regDate = LocalDateTime.now();

        public void changeMessageRequest(String chatMessageSeq, String message, String type) {
            this.chatMessageSeq = chatMessageSeq;
            this.message = message;
            this.type = type;
        }

    }

    @Builder
    public static class Update {

        private String chatMessageSeq;
        private String message;
        @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
        private LocalDateTime modDate;

    }

    @Getter
    public static class Response {

        private String chatMessageSeq;
        private String chatRoomSeq;
        private String type;
        private String message;
        private String sendSeq;
        private String senderNickName;
        private String receiveSeq;
        private LocalDateTime regDate;
        private LocalDateTime modDate;
        private String readYn;

    }

}
