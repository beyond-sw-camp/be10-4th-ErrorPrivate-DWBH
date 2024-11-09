package com.dwbh.backend.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    public static String getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) { return null;}  // 비회원이거나 예상치 않은 Principal일 경우 null 반환
        else { return ((UserDetails) authentication.getPrincipal()).getUsername(); }
    }
}
