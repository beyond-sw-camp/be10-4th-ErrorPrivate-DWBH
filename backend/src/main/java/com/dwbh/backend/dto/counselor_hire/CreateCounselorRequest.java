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
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCounselorRequest {
    private Long hireSeq;                    // 게시글 번호
    private String hireTitle;                // 게시글 제목
    private String hireContent;              // 게시글 내용
    private Gender hireGender;               // 희망 성별
    private String userNickname;             // 작성자 닉네임
    private LocalDateTime regDate;           // 작성일
    private List<Long>  ageRangeId;          // 선택한 나이대 ID
    private List<Long>  typeIds;             // 선택한 상담 유형 ID
    private List<CounselorAgeDTO> ageRanges; // 연령대 리스트
    private List<CounselorTypeDTO> types;    // 상담 유형 리스트
}

