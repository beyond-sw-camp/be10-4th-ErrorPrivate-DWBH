package com.dwbh.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
public class UserDetailResponse {
    private String filePath;
    private LocalDate userBirthday;
    private String userNickname;
    private String userMbti;
    private String userGender;
    private BigDecimal userTemperature;
}
