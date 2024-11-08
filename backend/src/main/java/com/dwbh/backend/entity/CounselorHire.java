package com.dwbh.backend.entity;

import com.dwbh.backend.common.entity.Gender;
import com.dwbh.backend.common.entity.BaseDateEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_counselor_hire")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@AttributeOverrides({
        @AttributeOverride(name = "regDate", column = @Column(name = "counselor_hire_reg_date")),
        @AttributeOverride(name = "modDate", column = @Column(name = "counselor_hire_mod_date"))
})
@SQLDelete(sql = "UPDATE tb_counselor_hire SET counselor_hire_del_date = NOW() WHERE counselor_hire_seq = ?")
public class CounselorHire extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "counselor_hire_seq")
    private Long hireSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @Column(name = "counselor_hire_title", nullable = false)
    private String hireTitle;

    @Column(name = "counselor_hire_content", nullable = false)
    private String hireContent;

    @Column(name = "counselor_hire_counselor_gender")
    @Enumerated(EnumType.STRING)
    private Gender hireGender;

    @OneToMany(mappedBy = "counselorHire")
    private List<CounselorHireAge> hireAges = new ArrayList<>();

    @OneToMany(mappedBy = "counselorHire")
    private List<CounselorHireType> hireTypes = new ArrayList<>();

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "counselor_age_range_seq", nullable = false)
//    private CounselorAge counselorAge;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "counselor_type_seq", nullable = false)
//    private CounselorType counselorType;

    @Column(name = "counselor_hire_del_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime delDate;

    @OneToMany(mappedBy = "hire", cascade = CascadeType.REMOVE)
    private List<CounselOffer> offers = new ArrayList<>();

    public void updateUser(User foundUser){
        this.user = foundUser;
    }

    public void updateCounselor(String counselorHireTitle, String counselorHireContent, String counselorHireCounselorGender) {
        this.hireTitle = counselorHireTitle;
        this.hireContent = counselorHireContent;
        this.hireGender = Gender.valueOf(counselorHireCounselorGender);
    }
}
