package com.dwbh.backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ModifyUserPasswordRequest {
    @NotBlank
    @Email
    private String userEmail;
    // 8자 이상, 알파뱃 대문자 또는 소문자 포함, 특수문자(@$!%*?&_) 포함
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&_])[A-Za-z\\d@$!%*?&_]{8,}$")
    @NotBlank
    private String userPassword;
}
