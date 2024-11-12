package com.dwbh.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_counselor_hire_age")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class CounselorHireAge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long counselorHireAgeSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_age_range_seq")
    private CounselorAge counselorAge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_hire_seq", nullable = false)
    private CounselorHire counselorHire;

    public void setCounselorHire(CounselorHire counselorHire) {
    }

    @Builder
    public CounselorHireAge(CounselorAge counselorAge, CounselorHire counselorHire) {
        this.counselorAge = counselorAge;
        this.counselorHire = counselorHire;
    }
}
