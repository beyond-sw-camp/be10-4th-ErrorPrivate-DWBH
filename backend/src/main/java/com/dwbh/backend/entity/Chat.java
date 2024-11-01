package com.dwbh.backend.entity;

import com.dwbh.backend.common.entity.BaseDateEntity;
import com.dwbh.backend.common.entity.YnType;
import com.dwbh.backend.common.util.DateTimeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity @Getter
@AttributeOverrides({
        @AttributeOverride(name = "regDate", column = @Column(name = "notification_reg_date")),
        @AttributeOverride(name = "modDate", column = @Column(name = "notification_mod_date"))
})
@Table(name = "tb_chat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat extends BaseDateEntity {

    @Id @Column(name = "chat_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatSeq;

    @Column(name = "counsel_offer_seq")
    private Long counselOfferSeq;

    @Column(name = "chat_send_seq")
    private Long sendSeq;

    @Column(name = "chat_receive_seq")
    private Long receiveSeq;

    @Column(name = "chat_del_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime delDate;

    @Column(name = "chat_read_yn")
    @Enumerated(EnumType.STRING)
    private YnType readYn = YnType.N;

    public void parseDate(String delDate) {
        this.delDate = DateTimeUtil.parse(delDate, "yyyy-MM-dd HH:mm:ss");
    }

}
