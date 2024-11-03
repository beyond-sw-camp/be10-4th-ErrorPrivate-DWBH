package com.dwbh.backend.entity;

import com.dwbh.backend.common.entity.BaseDateEntity;
import com.dwbh.backend.common.entity.YnType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AttributeOverrides({
        @AttributeOverride(name = "regDate", column = @Column(name = "notification_reg_date")),
        @AttributeOverride(name = "modDate", column = @Column(name = "notification_mod_date"))
})
@Getter
@Table(name = "tb_notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationSeq;

    private Long chatSeq;
    private Long userSeq;
    private YnType notificationCheckYn = YnType.N;

    public void updateNotificationCheckYn() {
        this.notificationCheckYn = YnType.Y;
    }
}
