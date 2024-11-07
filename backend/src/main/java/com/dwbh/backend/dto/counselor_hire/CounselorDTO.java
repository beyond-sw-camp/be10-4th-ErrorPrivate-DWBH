package com.dwbh.backend.dto.counselor_hire;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CounselorDTO {
    private Long hireSeq;           // 게시글 번호
    private String hireTitle;       // 게시글 제목
    private String hireContent;     // 게시글 내용
    private String hireGender;      // 희망 성별
    private Long ageRangeId;
    private Long typeId;
    private String createdDate;     // 작성일
}
