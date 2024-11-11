package com.dwbh.backend.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
public class UserModifyResponse {
    private String userEmail;
    private String userNickname;
    private String userGender;
    private LocalDate userBirthday;
    private String userMbti;
    private BigDecimal userTemperature;
    private LocalDateTime regDate;
}
