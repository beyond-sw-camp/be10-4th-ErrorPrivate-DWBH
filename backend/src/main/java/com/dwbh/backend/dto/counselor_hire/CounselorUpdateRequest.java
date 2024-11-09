package com.dwbh.backend.dto.counselor_hire;

import com.dwbh.backend.common.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CounselorUpdateRequest {
    private Long hireSeq;           // 게시글 번호
    private String hireContent;
    private String hireTitle;       // 게시글 제목
    private Gender hireGender;      // 희망 성별
}
