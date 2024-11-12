package com.dwbh.backend.dto.counselor_hire;

import com.dwbh.backend.common.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CounselorDetailResponse {

    private Long hireSeq;               // 게시글 번호
    private Long userSeq;               // 글작성자 번호
    private String userNickname;          // 글작성자 닉네임
    private String hireTitle;           // 게시글 제목
    private String hireContent;         // 게시글 내용
    private Gender hireGender;          // 희망 성별
    private List<String> counselorAgeRanges; // 희망 나이대
    private List<String> counselorTypes;       // 희망 조언유형
    private List<CounselorTypeDTO> counselorTypeDTOs;
    private LocalDateTime regDate;             // 작성일
    private LocalDateTime modDate;             // 수정일
    private LocalDateTime delDate;             // 삭제일
    private String hireFilePath;        // 첨부파일
}
