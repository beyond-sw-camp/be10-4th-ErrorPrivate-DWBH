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

    @Column(name = "counselor_hire_seq")
    private Long hireSeq;

    @Column(name = "file_seq")
    private Long fileSeq;

}
