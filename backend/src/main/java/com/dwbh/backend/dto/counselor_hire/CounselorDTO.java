package com.dwbh.backend.dto.counselor_hire;

import com.dwbh.backend.common.entity.Gender;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
@ToString
public class CounselorDTO {
    private Long hireSeq;           // 게시글 번호
    private String hireTitle;       // 게시글 제목
    private Gender hireGender;      // 희망 성별
    private String userNickname;
    private LocalDateTime regDate;     // 작성일
    private Long counselorAgeRangeSeq;
    private String counselorAgeRange;
    private Long counselorTypeSeq;
    private String counselorType;
    private List<CounselorAgeDTO> ageRanges;
    private List<CounselorTypeDTO> types;

    public CounselorDTO(Long hireSeq, String hireTitle, Gender hireGender, String userNickname, LocalDateTime regDate, Long counselorAgeRangeSeq, String counselorAgeRange, Long counselorTypeSeq, String counselorType) {
        this.hireSeq = hireSeq;
        this.hireTitle = hireTitle;
        this.hireGender = hireGender;
        this.userNickname = userNickname;
        this.regDate = regDate;
        this.counselorAgeRangeSeq = counselorAgeRangeSeq;
        this.counselorAgeRange = counselorAgeRange;
        this.counselorTypeSeq = counselorTypeSeq;
        this.counselorType = counselorType;
    }

    public CounselorDTO(Long hireSeq, String hireTitle, Gender hireGender, String userNickname, LocalDateTime regDate, List<CounselorAgeDTO> ageRanges, List<CounselorTypeDTO> types) {
        this.hireSeq = hireSeq;
        this.hireTitle = hireTitle;
        this.hireGender = hireGender;
        this.userNickname = userNickname;
        this.regDate = regDate;
        this.ageRanges = ageRanges;
        this.types = types;
    }
}
