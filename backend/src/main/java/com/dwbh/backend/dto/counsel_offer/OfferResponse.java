package com.dwbh.backend.dto.counsel_offer;

import com.dwbh.backend.common.entity.Gender;
import com.dwbh.backend.common.entity.YnType;
import com.querydsl.core.types.dsl.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "댓글 작성/수정 후 응답 DTO")
public class OfferResponse {

    private long offerSeq;  // 댓글 번호
    private long hireSeq;   // 게시글 번호
    private long userSeq;   // 댓글 작성자

    private String offerContent;    // 댓글 내용
    private YnType offerPrivateYn;  // 비밀 댓글 여부
    private String offerFilePath; // 댓글파일

    private LocalDateTime regDate;     // 댓글 작성일
    private LocalDateTime modDate;     // 댓글 수정일
//    private LocalDateTime delDate;     // 댓글 삭제일

    private String userNickname;
    private String userGender;
    private LocalDate userBirthday;
    private String userStatus;
    private String userProfilePath;

}
