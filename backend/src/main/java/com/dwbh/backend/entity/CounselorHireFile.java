package com.dwbh.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_counselor_hire_file")
@Getter
@NoArgsConstructor
public class CounselorHireFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "counselor_hire_file_seq")
    private Long hireFileSeq;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "file_seq", nullable = false)
    private File file;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_hire_seq", nullable = false)
    private CounselorHire counselorHire;
}
