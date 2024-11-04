package com.dwbh.backend.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class LoginRequest {
    private String email;
    private String password;
}
