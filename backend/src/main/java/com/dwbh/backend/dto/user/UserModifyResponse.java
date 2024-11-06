package com.dwbh.backend.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
public class UserModifyResponse {
    private String userEmail;
    private String userNickname;
    private String userGender;
    private String userBirthday;
    private String userMbti;
    private BigDecimal userTemperature;
    private LocalDate userRegDate;
}
