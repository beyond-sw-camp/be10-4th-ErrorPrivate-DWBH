package com.dwbh.backend.dto.offer;

import com.dwbh.backend.common.entity.YnType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "댓글 작성/수정 후 응답 DTO")
public class OfferDTO {

    private long offerSeq;  // 댓글 번호
    private long hireSeq;   // 게시글 번호
    private long userSeq;   // 댓글 작성자
    private String offerContent;    // 댓글 내용
    private YnType offerPrivateYn;  // 비밀 댓글 여부
    private LocalDateTime offerRegDate;     // 댓글 작성일
    private LocalDateTime offerModDate;     // 댓글 수정일

    private UserSummaryDTO userSummary; // 회원정보(닉네임, 성별, 나이대, 사진)

}
