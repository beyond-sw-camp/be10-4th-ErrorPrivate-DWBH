package com.dwbh.backend.entity;

import com.dwbh.backend.common.entity.BaseDateEntity;
import com.dwbh.backend.common.entity.YnType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@AttributeOverrides({
        @AttributeOverride(name = "regDate", column = @Column(name = "counsel_offer_reg_date")),
        @AttributeOverride(name = "modDate", column = @Column(name = "counsel_offer_mod_date"))
})
@Getter
@Table(name = "tb_counsel_offer")
@ToString
public class CounselOffer extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long counselOfferSeq;

    private Long counselorHireSeq;

    @ManyToOne
    @JoinColumn(name = "userSeq")
    private User user;

    private String counselOfferContent;
    @Enumerated(EnumType.STRING)
    private YnType counselOfferPrivateYn;
    private LocalDateTime counselOfferDelDate;
}
