package com.dwbh.backend.dto.user;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
public class ModifyUserRequest {
    private String userNickname;
    // 8자 이상, 알파뱃 대문자 또는 소문자 포함, 특수문자(@$!%*?&_) 포함
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&_])[A-Za-z\\d@$!%*?&_]{8,}$")
    private String userPassword;
    private String userGender;
    private LocalDate userBirthday;
    private String userMbti;
}
