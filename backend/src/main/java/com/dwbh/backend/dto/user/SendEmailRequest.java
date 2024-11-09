package com.dwbh.backend.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class SendEmailRequest {
    @Email
    private String email;
}
