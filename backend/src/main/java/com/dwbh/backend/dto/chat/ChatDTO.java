package com.dwbh.backend.dto.chat;

import com.dwbh.backend.common.entity.YnType;
import com.dwbh.backend.dto.user.UserDTO;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class ChatDTO {

    private Long chatSeq;
    private Long counselOfferSeq;
    private Long sendSeq;
    private Long receiveSeq;
    private LocalDateTime modDate;
    private LocalDateTime endDate;
    private YnType readYn;

    @Getter
    public static class Search {

        private Long userSeq;
        private YnType readYn;

    }

    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class Create {

        private Long counselOfferSeq;
        private Long sendSeq;
        private Long receiveSeq;

    }

    @Getter @Builder
    public static class Update {

        private Long chatSeq;
        private YnType readYn = YnType.N;
        private LocalDateTime modDate;
        private LocalDateTime endDate;

    }

    @Getter
    public static class Response {

        private Long chatSeq;
        private UserDTO sendUser;
        private UserDTO receiveUser;
        @Setter
        private String lastMessage;
        @Setter
        private boolean showEvaluation = false;
        @Setter
        private YnType readYn = YnType.N;
        private LocalDateTime modDate;
        private LocalDateTime endDate;

    }

}
