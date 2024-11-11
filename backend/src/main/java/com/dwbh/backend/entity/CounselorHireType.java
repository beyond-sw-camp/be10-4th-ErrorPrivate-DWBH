package com.dwbh.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_counselor_hire_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CounselorHireType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long counselorHireTypeSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_type_seq")
    private CounselorType counselorType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_hire_seq")
    private CounselorHire counselorHire;

    public void setCounselorHire(CounselorHire counselorHire) {
    }
}