package com.dwbh.backend.entity;

import com.dwbh.backend.common.entity.YnType;
import com.dwbh.backend.common.entity.BaseDateEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_counsel_offer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)  // 엔티티 생성, 삭제 시점 체크를 위해 필요한 리스너
@AttributeOverrides({
        @AttributeOverride(name = "regDate", column = @Column(name = "counsel_offer_reg_date")),
        @AttributeOverride(name = "modDate", column = @Column(name = "counsel_offer_mod_date"))
})
@SQLDelete(sql = "UPDATE tb_counsel_offer SET counsel_offer_del_date = NOW() WHERE counsel_offer_seq = ?")
public class CounselOffer extends BaseDateEntity {

    @Id
    @Column(name = "counsel_offer_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long offerSeq;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "counselor_hire_seq", nullable = false)
//    private CounselorHire hireSeq;
    @Column(name = "counselor_hire_seq", nullable = false)
    private Long hireSeq;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_seq", nullable = false)
//    private User userSeq;
    @Column(name = "user_seq", nullable = false)
    private Long userSeq;

    @Column(name = "counsel_offer_content", nullable = false)
    private String offerContent;

    @Column(name = "counsel_offer_private_yn", nullable = false)
    @Enumerated(EnumType.STRING)
    private YnType offerPrivateYn;

    @Column(name = "counsel_offer_del_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime delDate;

    @OneToOne(mappedBy = "counselOffer", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = true)   // 파일 선택, 작성,수정시
    private CounselOfferFile offerFile;

    // 파일 첨부를 위한 메서드
    public void inputOfferFile(CounselOfferFile offerFile) {
        this.offerFile = offerFile;
    }

    // 게시글 작성 시 userSeq를 Entity 에 담는 메서드
//    public void putUserSeq(User userSeq) {
//        this.userSeq = userSeq;
//    }
    public void putUserSeq(long userSeq) {
        this.userSeq = userSeq;
    }

    // 게시글 작성 시 hireSeq Entity 에 담는 메서드
//    public void putHireSeq(CounselorHire hireSeq) {
//        this.hireSeq = hireSeq;
//    }
    public void putHireSeq(long hireSeq) {
        this.hireSeq = hireSeq;
    }


}
