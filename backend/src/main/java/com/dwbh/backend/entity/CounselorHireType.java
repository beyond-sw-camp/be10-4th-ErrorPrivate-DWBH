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

    @ManyToOne
    @JoinColumn(name = "counselorTypeSeq")
    private CounselorType counselorType;

    @ManyToOne
    @JoinColumn(name = "counselorHireSeq")
    private CounselorHire counselorHire;
}
