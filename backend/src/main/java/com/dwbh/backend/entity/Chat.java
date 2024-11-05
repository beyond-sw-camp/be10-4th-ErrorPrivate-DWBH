package com.dwbh.backend.entity;

import com.dwbh.backend.common.entity.BaseDateEntity;
import com.dwbh.backend.common.entity.YnType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity @Getter
@Table(name = "tb_chat")
@AttributeOverrides({
        @AttributeOverride(name = "regDate", column = @Column(name = "chat_reg_date")),
        @AttributeOverride(name = "modDate", column = @Column(name = "chat_mod_date"))
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatSeq;

    @OneToOne
    @JoinColumn(name = "counselOfferSeq")
    private CounselOffer counselOffer;

    @ManyToOne
    @JoinColumn(name = "chatSendSeq", referencedColumnName = "userSeq")
    private User sendUser;

    @ManyToOne
    @JoinColumn(name = "chatReceiveSeq", referencedColumnName = "userSeq")
    private User receiveUser;

    @Column(name = "chat_end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endDate;

    @Column(name = "chat_read_yn")
    @Enumerated(EnumType.STRING)
    private YnType readYn = YnType.N;

    public void builder(CounselOffer counselOffer, User sendUser, User receiveUser) {
        this.counselOffer = counselOffer;
        this.sendUser = sendUser;
        this.receiveUser = receiveUser;
    }

}
