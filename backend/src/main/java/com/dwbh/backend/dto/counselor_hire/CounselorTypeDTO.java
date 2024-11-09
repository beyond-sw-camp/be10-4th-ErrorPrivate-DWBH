package com.dwbh.backend.dto.counselor_hire;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Slf4j
public class CounselorTypeDTO {
    private Long counselorTypeSeq;
    private String counselorType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CounselorTypeDTO that = (CounselorTypeDTO) o;
        return Objects.equals(counselorTypeSeq, that.counselorTypeSeq) &&
                Objects.equals(counselorType, that.counselorType); // 비교 기준 필드
    }

    @Override
    public int hashCode() {
        return Objects.hash(counselorTypeSeq, counselorType); // 비교 기준 필드
    }
}
