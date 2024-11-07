package com.dwbh.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_counselor_age")
@Getter
@NoArgsConstructor
public class CounselorAge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long counselorAgeRangeSeq;

    @Column(name = "counselor_age_range", nullable = false)
    private String ageRange;
}
