package com.dwbh.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_counselor_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CounselorType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long counselorTypeSeq;

    private String counselorType;
}