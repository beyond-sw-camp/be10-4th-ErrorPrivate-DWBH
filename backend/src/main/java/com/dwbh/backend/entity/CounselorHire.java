package com.dwbh.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_counselor_hire")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CounselorHire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long counselorHireSeq;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private User user;
    private String counselorHireTitle;
    private String counselorHireContent;
    private String counselorHireCounselorGender;

    private LocalDateTime counselorHireRegDate;
    private LocalDateTime counselorHireModDate;
    private LocalDateTime counselorHireDelDate;

    @PrePersist
    protected void onCreate() {
        counselorHireRegDate = LocalDateTime.now();
    }

    public void updateUser(User foundUser) {
        this.user = foundUser;
    }

    public void setCounselorHireTitle(String counselorHireTitle) {
    }

    public void setCounselorHireContent(String counselorHireContent) {
    }

    public void setCounselorHireCounselorGender(String counselorHireCounselorGender) {
    }
}
