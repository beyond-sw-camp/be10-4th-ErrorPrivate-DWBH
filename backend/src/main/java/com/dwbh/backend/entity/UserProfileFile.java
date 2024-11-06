package com.dwbh.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity @Getter
@Table(name = "tb_user_profile_file")
public class UserProfileFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProfileSeq;
    private Long fileSeq;
    private Long userSeq;

}
