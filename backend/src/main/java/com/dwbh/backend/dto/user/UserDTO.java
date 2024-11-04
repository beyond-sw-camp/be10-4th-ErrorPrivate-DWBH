package com.dwbh.backend.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
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
