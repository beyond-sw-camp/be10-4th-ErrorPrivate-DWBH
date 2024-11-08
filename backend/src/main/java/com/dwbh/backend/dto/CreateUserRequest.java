package com.dwbh.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CreateUserRequest {
    @NotBlank @Email
    private String userEmail;
    // 10자 이상, 알파뱃 대소문자 포함, 특수문자(@$!%*?&_) 포함
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_])[A-Za-z\\d@$!%*?&_]{10,}$")
    @NotBlank
    private String userPassword;
    @NotBlank
    private String userNickname;
    private String userGender;
    private LocalDate userBirthday;
    private String userMbti;
}
