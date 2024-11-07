package com.dwbh.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_counselor_type")
@Getter
@NoArgsConstructor
public class CounselorType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long counselorTypeSeq;

    @Column(name = "counselor_type", nullable = false)
    private String typeName;
}
