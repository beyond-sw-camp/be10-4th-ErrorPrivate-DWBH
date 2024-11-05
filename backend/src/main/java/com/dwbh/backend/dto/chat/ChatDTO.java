package com.dwbh.backend.dto.chat;

import com.dwbh.backend.common.entity.YnType;
import lombok.Data;
import lombok.Getter;

@Data
public class ChatDTO {

    private Long chatSeq;
    private Long counselOfferSeq;
    private Long sendSeq;
    private Long receiveSeq;
    private String delDate;
    private YnType readYn;

    @Getter
    public static class ChatRequestDTO {
        private Long counselOfferSeq;
        private Long sendSeq;
        private Long receiveSeq;
    }

    public static class ChatResponseDTO {

        private Long chatSeq;
        private Long sendSeq;
        private Long receiveSeq;
        private String delDate;
        private YnType readYn;
        private String sendUserName;
        private String lastMessage;
        private boolean showEvaluation;

    }

    public static class ChatEndDTO {

        private String delDate;

    }

    public static class ChatReadDTO {

        private YnType readYn;

    }

}
