package com.dwbh.backend.entity;

import com.dwbh.backend.common.entity.YnType;
import com.dwbh.backend.common.util.DateTimeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity @Getter
@Table(name = "tb_chat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

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

    @CreationTimestamp
    @Column(name = "chat_reg_date", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime chatRegDate;

    @Column(name = "chat_del_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime chatDelDate;

    @Column(name = "chat_read_yn")
    @Enumerated(EnumType.STRING)
    private YnType readYn = YnType.N;

    public void parseDate(String delDate) {
        this.chatDelDate = DateTimeUtil.parse(delDate, "yyyy-MM-dd HH:mm:ss");
    }

    public void builder(CounselOffer counselOffer, User sendUser, User receiveUser) {
        this.counselOffer = counselOffer;
        this.sendUser = sendUser;
        this.receiveUser = receiveUser;
    }
}
