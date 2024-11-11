package com.dwbh.backend.controller;

import com.dwbh.backend.common.util.AuthUtil;
import com.dwbh.backend.common.util.JwtUtil;
import com.dwbh.backend.dto.CreateUserRequest;
import com.dwbh.backend.dto.UserDetailResponse;
import com.dwbh.backend.dto.user.ModifyUserPasswordRequest;
import com.dwbh.backend.dto.user.ModifyUserRequest;
import com.dwbh.backend.dto.user.SendEmailRequest;
import com.dwbh.backend.dto.user.UserModifyResponse;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.service.EmailService;
import com.dwbh.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "User", description = "회원")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/user")
    @Operation(summary = "회원 등록")
    public ResponseEntity<Void> createUser(
            HttpServletRequest request,
            @Valid @RequestBody CreateUserRequest createUserRequest) {

        String emailHeader = request.getHeader("Email-Verify-Header");

        if (jwtUtil.validateToken(emailHeader) &&
                emailService.verifyEmail(jwtUtil.parseClaims(emailHeader).get("email").toString())) {
            userService.createUser(createUserRequest, jwtUtil.parseClaims(emailHeader).get("email").toString());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            throw new CustomException(ErrorCodeType.USER_EMAIL_TOKEN_ERROR);
        }
    }

    @GetMapping("/user/email-check/{userEmail}")
    @Operation(summary = "이메일 중복 확인")
    public ResponseEntity<Boolean> emailCheck(
            @PathVariable @Valid @Email String userEmail) {

        return ResponseEntity.ok(userService.emailCheck(userEmail));
    }

    @GetMapping("/user/nickname-check/{userNickname}")
    @Operation(summary = "닉네임 중복 확인")
    public ResponseEntity<Boolean> nicknameCheck(
            @PathVariable String userNickname) {

        return ResponseEntity.ok(userService.nicknameCheck(userNickname));
    }

    @GetMapping("/user/{userSeq}")
    @Operation(summary = "회원 상세 조회")
    public ResponseEntity<UserDetailResponse> getUser(
            @PathVariable Long userSeq) {
        UserDetailResponse response = userService.getUserDetail(userSeq);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userSeq}/modify")
    @Operation(summary = "회원 정보 수정 조회")
    public ResponseEntity<UserModifyResponse> getModifyUser(
            @PathVariable Long userSeq) {
        if (userService.getUserSeq(AuthUtil.getAuthUser()).equals(userSeq)) {
            return ResponseEntity.ok(userService.getUserModify(userSeq));
        } else { throw new CustomException(ErrorCodeType.SECURITY_ACCESS_ERROR); }
    }

    @PutMapping(value = "/user", consumes = {"multipart/form-data"})
    @Operation(summary = "회원 정보 수정")
    public ResponseEntity<Void> modifyUser(
            @Valid @RequestPart ModifyUserRequest modifyUserRequest,
            @RequestPart(required = false) MultipartFile userProfile) {
        userService.modifyUser(userService.getUserSeq(AuthUtil.getAuthUser()), userProfile, modifyUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/user")
    @Operation(summary = "회원 탈퇴")
    public ResponseEntity<Void> deleteUser() {
        userService.deleteUser(userService.getUserSeq(AuthUtil.getAuthUser()));

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/user/email")
    @Operation(summary = "이메일 인증 메일 전송")
    public ResponseEntity<Void> sandEmail(
            @RequestBody @Valid SendEmailRequest sendEmailRequest) {

        emailService.sendEmail(sendEmailRequest);

        return null;
    }

    @GetMapping("/user/email")
    @Operation(summary = "이메일 인증 코드 검증")
    public ResponseEntity<Void> verifyEmail(
            @RequestParam @Valid @Email String email,
            @RequestParam String code) {

        return ResponseEntity.status(HttpStatus.OK)
                .header("verifyToken", emailService.verifyEmailCode(email, code))
                .build();
    }

    @PutMapping("/user/password")
    @Operation(summary = "회원 패스워드 변경")
    public ResponseEntity<Void> updateUserPassword(
            HttpServletRequest request,
            @Valid @RequestBody ModifyUserPasswordRequest modifyUserPasswordRequest) {

        String emailHeader = request.getHeader("Email-Verify-Header");

        if (jwtUtil.validateToken(emailHeader) &&
                emailService.verifyEmail(jwtUtil.parseClaims(emailHeader).get("email").toString())) {

            userService.updateUserPassword(modifyUserPasswordRequest);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            throw new CustomException(ErrorCodeType.USER_EMAIL_TOKEN_ERROR);
        }
    }
}
