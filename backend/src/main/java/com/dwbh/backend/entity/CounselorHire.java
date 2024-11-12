package com.dwbh.backend.entity;

import com.dwbh.backend.common.entity.Gender;
import com.dwbh.backend.common.entity.BaseDateEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
@ToString
public class CounselorHire extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "counselor_hire_seq")
    private Long hireSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userSeq", nullable = false)
    private User user;

    @Column(name = "counselor_hire_title", nullable = false)
    private String hireTitle;

    @Column(name = "counselor_hire_content", nullable = false)
    private String hireContent;

    @Column(name = "counselor_hire_counselor_gender")
    @Enumerated(EnumType.STRING)
    private Gender hireGender;

    @OneToMany(mappedBy = "counselorHire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CounselorHireAge> hireAges = new ArrayList<>();

    @OneToMany(mappedBy = "counselorHire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CounselorHireType> hireTypes = new ArrayList<>();

    @Column(name = "counselor_hire_del_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime delDate;

    @OneToMany(mappedBy = "hire", cascade = CascadeType.REMOVE)
    private List<CounselOffer> offers = new ArrayList<>();

    @OneToOne(mappedBy = "counselorHire", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true, optional = true)
    private CounselorHireFile counselorHireFile;

    // 필수 필드를 포함한 생성자
    public CounselorHire(String hireTitle, String hireContent, Gender hireGender, User user, List<CounselorAge> counselorAge, List<CounselorType> counselorType) {
        this.hireTitle = hireTitle;
        this.hireContent = hireContent;
        this.hireGender = hireGender;
        this.user = user;
    }

    // 게시글 정보 업데이트 메서드
    public void updateCounselor(String counselorHireTitle, String counselorHireContent, Gender counselorHireCounselorGender) {
        this.hireTitle = counselorHireTitle;
        this.hireContent = counselorHireContent;
        this.hireGender = counselorHireCounselorGender;
    }

    // 삭제 날짜 설정 메서드 (논리 삭제)
    public void setDelDate(LocalDateTime delDate) {
        this.delDate = delDate;
    }

    public void builder(User user) {
        this.user = user;
    }

}
