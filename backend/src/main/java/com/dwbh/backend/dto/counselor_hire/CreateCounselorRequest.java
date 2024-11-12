package com.dwbh.backend.dto.counselor_hire;

import com.dwbh.backend.common.entity.Gender;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class CreateCounselorRequest {
    private String hireTitle;                // 게시글 제목
    private String hireContent;              // 게시글 내용
    private List<Long> hireAges; // 연령대 리스트
    private Gender hireGender;               // 희망 성별
    private Long hireTypes;    // 상담 유형 리스트
    private Long userSeq;
}

