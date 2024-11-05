package com.dwbh.backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCodeType {

    // 게이트웨이 관련 오류
    GATE_WAY_BAD_GATEWAY_ERROR(HttpStatus.BAD_GATEWAY, "GATEWAY_ERROR_001", "서버가 응답하지 않습니다."),
    GATE_WAY_TIMEOUT_ERROR(HttpStatus.GATEWAY_TIMEOUT, "GATEWAY_ERROR_002", "게이트웨이 시간 초과"),

    // 스프링 시큐리티 관련 오류
    SECURITY_ACCESS_ERROR(HttpStatus.FORBIDDEN, "SECURITY_ERROR_001", "접근 권한이 없습니다."),
    SECURITY_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "SECURITY_ERROR_002", "로그인 후 다시 시도해 주세요."),
    SECURITY_LOGIN_ERROR(HttpStatus.BAD_REQUEST, "SECURITY_ERROR_003", "로그인 실패하였습니다. 관리자에게 문의해 주세요."),

    // user 관련 오류
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_ERROR_001", "사용자를 찾을 수 없습니다."),

    // chat 관련 오류
    CHAT_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT_ERROR_001", "채팅방을 찾을 수 없습니다."),
    CHAT_CREATE_ERROR(HttpStatus.BAD_REQUEST, "CHAT_ERROR_002", "채팅방 생성에 실패하였습니다."),

    // 알림 관련 오류
    NOTICE_ERROR(HttpStatus.BAD_REQUEST, "NOTICE_ERROR_001", "알림 발송에 실패하였습니다."),
    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTICE_ERROR_002", "알림을 찾을 수 없습니다."),

    // 공통 오류
    COMMON_ERROR(HttpStatus.BAD_REQUEST, "COMMON_ERROR", "오류가 발생하였습니다. 관리자에게 문의 바랍니다."),

    COUNSEL_OFFER_NOT_FOUND(HttpStatus.NOT_FOUND, "COUNSEL_OFFER_ERROR_001", "댓글을 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}