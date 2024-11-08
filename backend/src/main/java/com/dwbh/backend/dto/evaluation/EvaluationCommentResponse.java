package com.dwbh.backend.dto.evaluation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "평가 댓글 응답 데이터")
public class EvaluationCommentResponse {

    private String offerContent;    // 댓글 내용
    private LocalDateTime regDate;     // 댓글 작성일
    private LocalDateTime modDate;     // 댓글 수정일

    private String userNickname;
    private String userGender;

    private String userProfilePath;
    private Integer userAge;
}
