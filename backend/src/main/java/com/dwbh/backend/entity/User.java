package com.dwbh.backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Getter
@Table(name = "tb_user")
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverrides({
        @AttributeOverride(name = "regDate", column = @Column(name = "user_reg_date")),
        @AttributeOverride(name = "modDate", column = @Column(name = "user_mod_date"))
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;
    private String userEmail;
    private String userPassword;
    private String userNickname;
    private String userGender;
    private LocalDate userBirthday;
    private String userMbti;
    private String userStatus;
    private BigDecimal userTemperature;
    private LocalDateTime userDelDate;

    // 패스워드 암호화
    public void encryptPassword(String encodedPwd) {
        this.userPassword = encodedPwd;
    }
}
