package com.dwbh.backend.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class ModifyUserRequest {
    private String userNickname;
    private String userPassword;
    private String userGender;
    private LocalDate userBirthday;
    private String userMbti;
}
