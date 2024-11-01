package com.dwbh.backend.dto.chat;

import lombok.Data;

@Data
public class ChatDTO {

    public Long chatSeq;

    public static class ChatRequestDTO {
        private String id;
        private String pw;
    }

    public static class ChatResponseDTO {
        private String id;
        private String pw;
    }

}
