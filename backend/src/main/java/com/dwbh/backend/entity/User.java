package com.dwbh.backend.entity;

import com.dwbh.backend.common.entity.BaseDateEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverrides({
        @AttributeOverride(name = "regDate", column = @Column(name = "user_reg_date")),
        @AttributeOverride(name = "modDate", column = @Column(name = "user_mod_date"))
})
@Table(name = "tb_user")
public class User extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    private String userEmail;
    private String userPassword;
    private String userNickname;
    private String userGender;
    private String userBirthday;
    private String userMbti;
    private String userStatus;

    private double userTemperature;
}
