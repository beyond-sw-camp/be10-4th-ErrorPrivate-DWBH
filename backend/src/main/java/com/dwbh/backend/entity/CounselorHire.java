package com.dwbh.backend.entity;

import com.dwbh.backend.common.entity.Gender;
import com.dwbh.backend.common.entity.BaseDateEntity;
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
@EntityListeners(AuditingEntityListener.class)  // 엔티티 생성, 삭제 시점 체크를 위해 필요한 리스너
@AttributeOverrides({
        @AttributeOverride(name = "regDate", column = @Column(name = "counselor_hire_reg_date")),
        @AttributeOverride(name = "modDate", column = @Column(name = "counselor_hire_mod_date"))
})
@SQLDelete(sql = "UPDATE tb_counselor_hire SET counselor_hire_del_date = NOW() WHERE counselor_hire_seq = ?")
public class CounselorHire extends BaseDateEntity {

    @Id
    @Column(name = "counselor_hire_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hireSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User userSeq;

    @Column(name = "counselor_hire_title", nullable = false)
    private String hireTitle;

    @Column(name = "counselor_hire_content", nullable = false)
    private String hireContent;

    @Column(name = "counselor_hire_counselor_gender")
    @Enumerated(EnumType.STRING)
    private Gender hireGender;

    @Column(name = "counselor_hire_del_date")
    private LocalDateTime delDate;

    @OneToMany(mappedBy = "hireSeq", cascade = CascadeType.REMOVE)
    private List<CounselOffer> offers = new ArrayList<>();


}
