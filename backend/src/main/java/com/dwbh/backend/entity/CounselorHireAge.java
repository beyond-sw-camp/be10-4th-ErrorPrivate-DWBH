package com.dwbh.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_counselor_hire_age")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CounselorHireAge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long counselorHireAgeSeq;

    @ManyToOne
    @JoinColumn(name = "counselor_age_range_seq")
    private CounselorAge counselorAge;

    @ManyToOne
    @JoinColumn(name = "counselor_hire_seq")
    private CounselorHire counselorHire;
}
