package com.dwbh.backend.dto.user;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class ModifyUserRequest {
    private String userNickname;
    // 10자 이상, 알파뱃 대소문자 포함, 특수문자(@$!%*?&_) 포함
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_])[A-Za-z\\d@$!%*?&_]{10,}$")
    private String userPassword;
    private String userGender;
    private LocalDate userBirthday;
    private String userMbti;
}
