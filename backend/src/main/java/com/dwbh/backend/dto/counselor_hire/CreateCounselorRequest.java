package com.dwbh.backend.dto.counselor_hire;

import com.dwbh.backend.common.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CreateCounselorRequest {
    private Long hireSeq;           // 게시글 번호
    private String hireTitle;       // 게시글 제목
    private Gender hireGender;      // 희망 성별
    private String userNickname;
    private LocalDateTime regDate;     // 작성일
    private List<CounselorAgeDTO> ageRanges;
    private List<CounselorTypeDTO> types;
}
