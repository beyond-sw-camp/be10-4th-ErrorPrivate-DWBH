package com.dwbh.backend.entity;

import com.dwbh.backend.common.entity.BaseDateEntity;
import com.dwbh.backend.dto.user.ModifyUserRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

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
@SQLDelete(sql = "UPDATE tb_user SET user_status = 'delete', user_del_date = NOW() WHERE user_seq = ?")
public class User extends BaseDateEntity {

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

    // 회원 정보 수정
    public void modifyUser(ModifyUserRequest modify, boolean modifyPassword) {
        this.userNickname = modify.getUserNickname();
        if (modifyPassword) this.userPassword = modify.getUserPassword();
        this.userGender = modify.getUserGender();
        this.userBirthday = modify.getUserBirthday();
        this.userMbti = modify.getUserMbti();
    }

    // 비밀번호 수정
    public void modifyUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
