package com.dwbh.backend.dto.counselor_hire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CounselorAgeDTO {
    private Long counselorAgeRangeSeq;
    private String counselorAgeRange;

    // 문자열 인자를 받는 추가 생성자
    public CounselorAgeDTO(String counselorAgeRange) {
        this.counselorAgeRange = counselorAgeRange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CounselorAgeDTO that = (CounselorAgeDTO) o;
        return Objects.equals(counselorAgeRangeSeq, that.counselorAgeRangeSeq) &&
                Objects.equals(counselorAgeRange, that.counselorAgeRange); // 비교 기준 필드
    }

    @Override
    public int hashCode() {
        return Objects.hash(counselorAgeRangeSeq, counselorAgeRange); // 비교 기준 필드
    }
}
