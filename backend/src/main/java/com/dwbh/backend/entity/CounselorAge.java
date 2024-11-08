package com.dwbh.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_counselor_age")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CounselorAge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long counselorAgeRangeSeq;

    private String counselorAgeRange;
}
