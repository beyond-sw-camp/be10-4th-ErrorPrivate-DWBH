package com.dwbh.backend.entity;

import com.dwbh.backend.common.aggregate.Gender;
import com.dwbh.backend.common.entity.BaseDateEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)  // 엔티티 생성, 삭제 시점 체크를 위해 필요한 리스너
@AttributeOverrides({
        @AttributeOverride(name = "regDate", column = @Column(name = "user_reg_date")),
})
@SQLDelete(sql = "UPDATE tb_user SET user_status = 'inactivate', user_del_date = NOW() WHERE user_seq = ?")
public class User extends BaseDateEntity {

    @Id
    @Column(name = "user_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    private String userEmail;
    private String userPassword;
    private String userNickname;
    private Gender userGender;
    private LocalDate userBirthday;
    private String userMBTI;
    private String userStatus;
    private Double userTemperature;
    private LocalDateTime delDate;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_seq", referencedColumnName = "user_seq")
//    private UserProfileFile userProfileFile;

}
