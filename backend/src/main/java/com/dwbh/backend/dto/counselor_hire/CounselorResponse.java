package com.dwbh.backend.dto.counselor_hire;

import com.dwbh.backend.common.entity.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CounselorResponse {
    private Long hireSeq;           // 게시글 번호
    private String hireTitle;       // 게시글 제목
    private Gender hireGender;      // 희망 성별
    private String userNickname;
    private LocalDateTime regDate;     // 작성일
    private List<CounselorAgeDTO> ageRanges;
    private List<CounselorTypeDTO> types;

    public CounselorResponse(Long hireSeq, String hireTitle, Gender hireGender, String userNickname, LocalDateTime regDate) {
        this.hireSeq = hireSeq;
        this.hireTitle = hireTitle;
        this.hireGender = hireGender;
        this.userNickname = userNickname;
        this.regDate = regDate;
    }
}
