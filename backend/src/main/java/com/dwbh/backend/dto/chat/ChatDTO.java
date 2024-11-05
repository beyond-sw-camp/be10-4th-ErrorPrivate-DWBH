package com.dwbh.backend.dto.chat;

import com.dwbh.backend.common.entity.YnType;
import com.dwbh.backend.dto.user.UserDTO;
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

    @Getter
    public static class ChatResponseDTO {

        private Long chatSeq;
        private String endDate;
        private String modDate;
        private YnType readYn;
        private UserDTO sendUser;
        private UserDTO receiveUser;
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
