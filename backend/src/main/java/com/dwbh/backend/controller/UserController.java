package com.dwbh.backend.controller;

import com.dwbh.backend.common.util.AuthUtil;
import com.dwbh.backend.dto.CreateUserRequest;
import com.dwbh.backend.dto.UserDetailResponse;
import com.dwbh.backend.dto.user.ModifyUserRequest;
import com.dwbh.backend.dto.user.UserModifyResponse;
import com.dwbh.backend.exception.CustomException;
import com.dwbh.backend.exception.ErrorCodeType;
import com.dwbh.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "User", description = "회원")
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    @Operation(summary = "회원 등록")
    public ResponseEntity<Void> createUser(
            @Validated @RequestBody CreateUserRequest createUserRequest) {

        userService.createUser(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
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

    @PutMapping(name = "/user", consumes = {"multipart/form-data"})
    @Operation(summary = "회원 정보 수정")
    public ResponseEntity<Void> modifyUser(
            @Valid @RequestPart ModifyUserRequest modifyUserRequest,
            @RequestPart(required = false) MultipartFile userProfile) {
        userService.modifyUser(userService.getUserSeq(AuthUtil.getAuthUser()), userProfile, modifyUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
