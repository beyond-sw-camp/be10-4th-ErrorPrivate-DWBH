package com.dwbh.backend.dto.chat;

import com.dwbh.backend.common.entity.YnType;
import com.dwbh.backend.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
public class ChatDTO {

    private Long chatSeq;
    private Long counselOfferSeq;
    private Long sendSeq;
    private Long receiveSeq;
    private String endDate;
    private YnType readYn;

    @Getter
    public static class Search {

        private Long userSeq;
        private YnType readYn;

    }

    @AllArgsConstructor
    @Getter
    public static class Create {

        private Long counselOfferSeq;
        private Long sendSeq;
        private Long receiveSeq;

    }

    @Getter
    public static class Response {

        private Long chatSeq;
        private String endDate;
        private String modDate;
        private YnType readYn;
        private UserDTO sendUser;
        private UserDTO receiveUser;
        private String lastMessage;
        private boolean showEvaluation;

    }

}
