package com.dwbh.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateUserRequest {
    @NotBlank
    private String userEmail;
    @NotBlank
    private String userPassword;
    @NotBlank
    private String userNickname;
    private String userGender;
    private LocalDate userBirthday;
    private String userMbti;
}
