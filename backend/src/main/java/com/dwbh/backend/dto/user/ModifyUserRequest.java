package com.dwbh.backend.dto.user;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ModifyUserRequest {
    private String filePath;
    private String userNickname;
    private String userPassword;
    private String userGender;
    private LocalDate userBirthday;
    private String userMbti;
}
